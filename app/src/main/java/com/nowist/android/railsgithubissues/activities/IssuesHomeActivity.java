package com.nowist.android.railsgithubissues.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nowist.android.railsgithubissues.BuildConfig;
import com.nowist.android.railsgithubissues.R;
import com.nowist.android.railsgithubissues.adapters.IssueAdapter;
import com.nowist.android.railsgithubissues.fragments.CommentsDialogFragment;
import com.nowist.android.railsgithubissues.models.IssueModel;
import com.nowist.android.railsgithubissues.retrofit.ApiUtils;
import com.nowist.android.railsgithubissues.retrofit.RestApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.app.DialogFragment.STYLE_NORMAL;
import static android.support.v4.app.DialogFragment.STYLE_NO_TITLE;

public class IssuesHomeActivity extends AppCompatActivity implements IssueAdapter.IssueAdapterOnClickListner {

    private static final String TAG = IssuesHomeActivity.class.getSimpleName();
    private RecyclerView mIssueRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private IssueAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_home);
        initControl();
        setUpRecyclerView();
        loadIssueData();
    }

    /**
     * Call the api to load the issue data
     */
    private void loadIssueData() {
        RestApiService restApiService = ApiUtils.getApiService();
        Call<ArrayList<IssueModel>> call = restApiService.getIssues();
        mLoadingIndicator.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ArrayList<IssueModel>>() {
            @Override
            public void onResponse(Call<ArrayList<IssueModel>> call, Response<ArrayList<IssueModel>> response) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if (response != null && response.body() != null) {
                    if (response.isSuccessful() && !response.body().isEmpty()) {
                        showIssueDataView();
                        mAdapter.setmIssueDataList(response.body());
                    } else {
                        showErrorMessage();
                    }
                } else {
                    //Display a toast
                    showErrorMessage();
                    Toast.makeText(IssuesHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<IssueModel>> call, Throwable t) {
                if (t != null && BuildConfig.DEBUG) {
                    t.printStackTrace();
                    Log.d(TAG, "Api Request Failed");
                }
            }
        });
    }

    private void showErrorMessage() {
        mIssueRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showIssueDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mIssueRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mIssueRecyclerView.setLayoutManager(linearLayoutManager);
        mIssueRecyclerView.setHasFixedSize(true);

        //initializing the adapter
        mAdapter = new IssueAdapter(this);
        mIssueRecyclerView.setAdapter(mAdapter);
    }

    private void initControl() {
        mIssueRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_issue);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called when a particular item is clicked
     * Here we need to call the comment uri
     *
     * @param commentUri
     */
    @Override
    public void onClick(String commentUri) {
        //Here we show the dialog fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        CommentsDialogFragment commentsDialogFragment = CommentsDialogFragment.newInstance(commentUri);
        commentsDialogFragment.setStyle(STYLE_NO_TITLE, R.style.Dialog_NoTitle);
        commentsDialogFragment.show(fragmentManager, "fragment_show_comments");
    }
}
