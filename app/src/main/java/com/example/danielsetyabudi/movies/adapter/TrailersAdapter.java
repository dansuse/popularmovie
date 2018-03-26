package com.example.danielsetyabudi.movies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danielsetyabudi.movies.R;
import com.example.danielsetyabudi.movies.contentprovider.MovieContract;
import com.example.danielsetyabudi.movies.model.Trailer;
import com.example.danielsetyabudi.movies.moviedetail.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by daniel on 22/07/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder>{
    private List<Trailer>mTrailerList;

    private MovieDetailActivity.TrailerItemListener mTrailerItemListener;
    private Context mContext;

    private boolean mModeCursor = false;
    private Cursor mCursor = null;

    public TrailersAdapter(Context context, MovieDetailActivity.TrailerItemListener trailerItemListener) {
        mTrailerItemListener = trailerItemListener;
        mContext = context;
    }

    public void replaceData(List<Trailer> trailerList){
        mModeCursor = false;
        tutupCursor();
        mTrailerList = trailerList;
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
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        if(mModeCursor){
            Trailer trailer = getItem(position);
            if(trailer != null){
                holder.bind(trailer);
            }
        }else{
            holder.bind(mTrailerList.get(position));
        }
    }

    private Trailer getItem(int position){
        if(mCursor != null && !mCursor.isClosed() && mCursor.moveToPosition(position)){
            Trailer trailer = new Trailer();
            trailer.setName(mCursor.getString(mCursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_NAME)));
            trailer.setKey(mCursor.getString(mCursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_KEY)));
            return trailer;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if(mModeCursor){
            return mCursor == null ? 0 : mCursor.getCount();
        }else{
            return mTrailerList == null ? 0 : mTrailerList.size();
        }
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTrailerNameTextView;
        private ImageView mTrailerImageView;
        TrailerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTrailerNameTextView = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            mTrailerImageView = (ImageView)itemView.findViewById(R.id.iv_trailer);
        }
        void bind(Trailer trailer){
            String youtubeThumbnailUrl = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";
            Picasso.with(mContext).load(youtubeThumbnailUrl).into(mTrailerImageView);
            mTrailerNameTextView.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(mModeCursor){
                Trailer trailer = getItem(position);
                if(trailer != null){
                    mTrailerItemListener.onTrailerClick(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                }
            }else{
                Trailer trailer = mTrailerList.get(position);
                mTrailerItemListener.onTrailerClick(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
            }
        }
    }
}
