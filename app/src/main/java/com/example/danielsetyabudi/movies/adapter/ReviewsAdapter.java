package com.example.danielsetyabudi.movies.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danielsetyabudi.movies.R;
import com.example.danielsetyabudi.movies.contentprovider.MovieContract;
import com.example.danielsetyabudi.movies.model.Review;

import java.util.List;

/**
 * Created by daniel on 22/07/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{

    private List<Review> mReviewList;
    private boolean mModeCursor = false;
    private Cursor mCursor = null;
    public ReviewsAdapter() {
    }

    public void replaceData(List<Review>reviewList){
        mModeCursor = false;
        tutupCursor();
        mReviewList = reviewList;
        notifyDataSetChanged();
    }
    public void swapCursor(Cursor cursor){
        mModeCursor = true;
        tutupCursor();
        mCursor = cursor;
        notifyDataSetChanged();
    }
    private void tutupCursor(){
        if(mCursor != null){
            mCursor.close();
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if(mModeCursor){
            Review review = getItem(position);
            if(review != null){
                holder.bind(review);
            }
        }else{
            holder.bind(mReviewList.get(position));
        }
    }

    private Review getItem(int position){
        if(mCursor != null && !mCursor.isClosed() && mCursor.moveToPosition(position)){
            Review review = new Review();
            review.setAuthor(mCursor.getString(mCursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR)));
            review.setContent(mCursor.getString(mCursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT)));
            return review;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if(mModeCursor){
            return mCursor == null ? 0 : mCursor.getCount();
        }else{
            return mReviewList == null ? 0 : mReviewList.size();
        }
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
