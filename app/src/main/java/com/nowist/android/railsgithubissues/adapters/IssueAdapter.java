package com.nowist.android.railsgithubissues.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nowist.android.railsgithubissues.R;
import com.nowist.android.railsgithubissues.models.IssueModel;

import java.util.ArrayList;

/**
 * Created by mayankverma on 09/07/17.
 */

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueAdapterViewHolder> {

    private ArrayList<IssueModel> mIssueDataList;
    private final IssueAdapterOnClickListner mClickHandler;

    public IssueAdapter(IssueAdapterOnClickListner clickListner) {
        mClickHandler = clickListner;
    }

    public void setmIssueDataList(ArrayList<IssueModel> mIssueDataList) {
        this.mIssueDataList = mIssueDataList;
        notifyDataSetChanged();
    }

    public interface IssueAdapterOnClickListner {
        void onClick(String commentUri);
    }

    @Override
    public IssueAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.issue_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new IssueAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IssueAdapterViewHolder holder, int position) {
        //Binding the issue data overhere
        IssueModel issue = mIssueDataList.get(position);
        if (issue != null) {
            holder.mTvIssueTitle.setText(issue.getTitle());
            holder.mTvIssueDesc.setText(issue.getBody());
        }
    }

    @Override
    public int getItemCount() {
        if (mIssueDataList != null && !mIssueDataList.isEmpty()) {
            return mIssueDataList.size();
        }
        return 0;
    }

    public class IssueAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTvIssueTitle, mTvIssueDesc;

        public IssueAdapterViewHolder(View itemView) {
            super(itemView);
            mTvIssueTitle = (TextView) itemView.findViewById(R.id.tv_issue_title);
            mTvIssueDesc = (TextView) itemView.findViewById(R.id.tv_issue_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPostion = getAdapterPosition();
            String commentUri = mIssueDataList.get(adapterPostion).getCommentsUrl();
            mClickHandler.onClick(commentUri);
        }
    }
}
