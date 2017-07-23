package com.example.danielsetyabudi.movies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.danielsetyabudi.movies.R;
import com.example.danielsetyabudi.movies.contentprovider.MovieContract;
import com.example.danielsetyabudi.movies.model.Movie;
import com.example.danielsetyabudi.movies.movies.MoviesActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by daniel on 23/07/2017.
 */

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>{
    Cursor mCursor;
    private Context mContext;
    private MoviesActivity.MovieItemListener mMovieItemListener;
    public FavoriteMovieAdapter(MoviesActivity.MovieItemListener movieItemListener, Context context) {
        mMovieItemListener = movieItemListener;
        mContext = context;
    }

    public void swapCursor(Cursor cursor){
        if(mCursor != null){
            mCursor.close();
        }
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public FavoriteMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new FavoriteMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        if(movie != null){
            holder.bind(movie);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public Movie getItem(int position){
        if(mCursor != null && !mCursor.isClosed() && mCursor.moveToPosition(position)){
            Movie movie = new Movie();
            movie.setId(mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry._ID)));
            movie.setOriginalTitle(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
            movie.setOverview(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
            movie.setPosterPath(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
            movie.setReleaseDate(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
            movie.setUserRating(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING)));
            return movie;
        }
        return null;
    }

    class FavoriteMovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        ImageView mPosterImageView;
        FavoriteMovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mPosterImageView = (ImageView)itemView.findViewById(R.id.iv_poster);
        }
        public void bind(Movie movie){
            Picasso.with(mContext).load(movie.getPosterPath()).into(mPosterImageView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = getItem(position);
            if(movie != null){
                mMovieItemListener.onMovieClick(movie.getId());
            }
        }
    }
}