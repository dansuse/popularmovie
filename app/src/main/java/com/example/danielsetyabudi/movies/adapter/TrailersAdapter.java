package com.example.danielsetyabudi.movies.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danielsetyabudi.movies.R;
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

    public TrailersAdapter(Context context, MovieDetailActivity.TrailerItemListener trailerItemListener) {
        mTrailerItemListener = trailerItemListener;
        mContext = context;
    }

    public void replaceData(List<Trailer> trailerList){
        mTrailerList = trailerList;
        notifyDataSetChanged();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(mTrailerList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTrailerList == null ? 0 : mTrailerList.size();
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
            Trailer trailer = mTrailerList.get(position);
            mTrailerItemListener.onTrailerClick(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        }
    }
}
