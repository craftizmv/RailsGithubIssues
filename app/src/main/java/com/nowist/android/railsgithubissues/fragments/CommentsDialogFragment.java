package com.nowist.android.railsgithubissues.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nowist.android.railsgithubissues.BuildConfig;
import com.nowist.android.railsgithubissues.R;
import com.nowist.android.railsgithubissues.models.Comment;
import com.nowist.android.railsgithubissues.retrofit.ApiUtils;
import com.nowist.android.railsgithubissues.retrofit.RestApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mayankverma on 09/07/17.
 */

public class CommentsDialogFragment extends DialogFragment {

    private String mCommentsUri;
    private ArrayList<Comment> mDataList;
    private boolean isCommentLoadFailed = false;
    private TextView mCommentsTv;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    public static final String TAG = CommentsDialogFragment.class.getSimpleName();
    private Call<ArrayList<Comment>> mCall;

    public static CommentsDialogFragment newInstance(String uri) {
        CommentsDialogFragment commentsDialogFragment = new CommentsDialogFragment();

        //supply comments as argument
        Bundle bundle = new Bundle();
        bundle.putString("comment_uri", uri);
        commentsDialogFragment.setArguments(bundle);
        return commentsDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommentsUri = getArguments().getString("comment_uri");
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    /**
     * Here call the api to fetch the comments data
     */
    private void loadCommentsData() {
        if (!TextUtils.isEmpty(mCommentsUri)) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            RestApiService restApiService = ApiUtils.getApiService();
            mCall = restApiService.getComments(mCommentsUri);
            mCall.enqueue(new Callback<ArrayList<Comment>>() {
                @Override
                public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                    mLoadingIndicator.setVisibility(View.GONE);
                    if (response != null && response.body() != null) {
                        if (response.isSuccessful() && !response.body().isEmpty()) {
                            mDataList = response.body();
                        } else {
                            isCommentLoadFailed = true;
                        }
                    } else {
                        isCommentLoadFailed = true;
                        Toast.makeText(getActivity(), " Data Load Failed ", Toast.LENGTH_SHORT).show();
                    }
                    setViewWithData();
                }

                @Override
                public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                    mLoadingIndicator.setVisibility(View.GONE);
                    isCommentLoadFailed = true;
                }
            });

        } else {
            Toast.makeText(getActivity(), "Provided issue id is not valid", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_layout, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initView(View view) {
        mCommentsTv = (TextView) view.findViewById(R.id.tv_comments);
        mErrorMessageDisplay = (TextView) view.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading);

        loadCommentsData();
    }

    private void setViewWithData() {
        if (mDataList != null && !mDataList.isEmpty() && !isCommentLoadFailed) {

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "  Comments Size is : " + mDataList.size());
            }

            for (Comment comment : mDataList) {
                if (comment != null && comment.getUser() != null && comment.getUser().getLogin() != null) {

//                    String str = "<b>" + comment.getUser().getLogin() + "</b> ";
//                    Spanned spannedStr;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                        spannedStr = Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY);
//                    } else {
//                        spannedStr = Html.fromHtml(str);
//                    }
                    mCommentsTv.append("Username : " + comment.getUser().getLogin() + " \n\n");
                }
                if ((comment != null && comment.getBody() != null)) {
                    mCommentsTv.append("Comment :  \n\n" + comment.getBody() + "\n\n");
                }
            }
        } else {
            //here we show the error view
            showErrorView();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void showErrorView() {
        mCommentsTv.setText(" No Comment Present");
        mCommentsTv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        mCommentsTv.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCall != null && mCall.isExecuted()) {
            mCall.cancel();
        }
    }
}
