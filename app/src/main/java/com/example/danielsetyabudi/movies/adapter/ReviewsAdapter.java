package com.example.danielsetyabudi.movies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danielsetyabudi.movies.R;
import com.example.danielsetyabudi.movies.model.Review;

import java.util.List;

/**
 * Created by daniel on 22/07/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{

    private List<Review> mReviewList;
    public ReviewsAdapter() {
    }

    public void replaceData(List<Review>reviewList){
        mReviewList = reviewList;
        notifyDataSetChanged();
    }
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(mReviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviewList == null ? 0 : mReviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{
        private TextView mAuthorTextView;
        private TextView mContentTextView;
        ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView)itemView.findViewById(R.id.tv_author);
            mContentTextView = (TextView)itemView.findViewById(R.id.tv_content);
        }
        void bind(Review review){
            mAuthorTextView.setText(review.getAuthor());
            mContentTextView.setText(review.getContent());
        }
    }
}
