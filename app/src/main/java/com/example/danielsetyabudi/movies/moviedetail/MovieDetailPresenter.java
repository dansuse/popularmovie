package com.example.danielsetyabudi.movies.moviedetail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.danielsetyabudi.movies.contentprovider.MovieContract;
import com.example.danielsetyabudi.movies.data.MoviesRepository;
import com.example.danielsetyabudi.movies.model.Movie;
import com.example.danielsetyabudi.movies.model.Review;
import com.example.danielsetyabudi.movies.model.Trailer;

import java.util.List;

/**
 * Created by Daniel Setyabudi on 30/06/2017.
 */
public class MovieDetailPresenter implements MovieDetailContract.UserActionsListener {

    private final MoviesRepository mMoviesRepository;
    private final MovieDetailContract.View mMovieDetailView;
    private Movie mMovie = null;
    private List<Trailer>mTrailerList = null;
    private List<Review>mReviewList = null;
    private Context mContext = null;
    private boolean mIsFavorite = false;

    public MovieDetailPresenter(@NonNull MoviesRepository moviesRepository, @NonNull MovieDetailContract.View movieDetailView, Context context) {
        mContext = context;
        mMoviesRepository = moviesRepository;
        mMovieDetailView = movieDetailView;
    }

    @Override
    public void openMovie(int mode, int movieId) {
        mMoviesRepository.getMovie(mode, movieId, new MoviesRepository.MoviesRepositoryCallback<Movie>() {
            @Override
            public void onResultLoaded(Movie movie) {
                mMovie = movie;
                if(movie == null){

                }else{
                    mMovieDetailView.setMovie(movie);
                    mMovieDetailView.setTitle(movie.getOriginalTitle());
                    //mMovieDetailView.setSubtitle(movie.getOriginalTitle());
                    mMovieDetailView.showMoviePoster(movie.getPosterPath());
                    mMovieDetailView.showMovieTitle(movie.getOriginalTitle());
                    mMovieDetailView.showReleaseDate(movie.getReleaseDate());
                    mMovieDetailView.showSynopsis(movie.getOverview());
                    mMovieDetailView.showUserRating(movie.getUserRating());
                }
            }
        });
        mMoviesRepository.getTrailers(mode, movieId, new MoviesRepository.MoviesRepositoryCallback<List<Trailer>>() {
            @Override
            public void onResultLoaded(List<Trailer> result) {
                mTrailerList = result;
                if(result.size() > 0) mMovieDetailView.showTrailers(result);
            }
        });
        mMoviesRepository.getReviews(mode, 1, movieId, new MoviesRepository.MoviesRepositoryCallback<List<Review>>() {
            @Override
            public void onResultLoaded(List<Review> result) {
                mReviewList = result;
                if(result.size() > 0) mMovieDetailView.showReviews(result);
            }
        });
    }

    @Override
    public void handleKlikMoviePosterImageView() {
        if(mMovie != null){
            mMovieDetailView.showDialogFragmentMoviePoster(mMovie.getPosterPath());
        }
    }

    @Override
    public void checkIfMovieIsFavorite(int movieId) {

        new AsyncTask<Integer, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Integer... params) {
                if(params[0] == null)return false;
                boolean isFavorite = false;
                Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon()
                        .appendPath(params[0].toString()).build();
                Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
                if(cursor != null && cursor.moveToFirst()){
                    isFavorite = true;
                    cursor.close();
                }
                else isFavorite=false;
                return isFavorite;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                mIsFavorite = aBoolean;
                mMovieDetailView.turnFavorite(aBoolean);
            }
        }.execute(movieId);
    }

    interface CallbackTask{
        public void onTaskFinish(int taskNumber);
    }
    CallbackTask mCallbackTask = new CallbackTask() {
        @Override
        public void onTaskFinish(int taskNumber) {
            if(!mIsFavorite){
                if(taskNumber == 1){//selesai insert movie
                    if(mTrailerList.size() > 0){
                        ContentValues[]contentValues = new ContentValues[mTrailerList.size()];
                        for(int i=0 ; i<mTrailerList.size() ; i++){
                            Trailer t = mTrailerList.get(i);
                            contentValues[i] = new ContentValues();
                            contentValues[i].put(MovieContract.TrailerEntry.COLUMN_KEY, t.getKey());
                            contentValues[i].put(MovieContract.TrailerEntry.COLUMN_NAME, t.getName());
                            contentValues[i].put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, mMovie.getId());
                        }
                        new InsertTrailersTask().execute(contentValues);
                        return;
                    }else if(mReviewList.size() > 0){
                        ContentValues[]contentValues = new ContentValues[mReviewList.size()];
                        for(int i=0 ; i<mReviewList.size() ; i++){
                            Review r = mReviewList.get(i);
                            contentValues[i] = new ContentValues();
                            contentValues[i].put(MovieContract.ReviewEntry.COLUMN_AUTHOR, r.getAuthor());
                            contentValues[i].put(MovieContract.ReviewEntry.COLUMN_CONTENT, r.getContent());
                            contentValues[i].put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, mMovie.getId());
                        }
                        new InsertReviewsTask().execute(contentValues);
                        return;
                    }
                }else if(taskNumber == 2){//selesai insert trailer
                    if(mReviewList.size() > 0){
                        ContentValues[]contentValues = new ContentValues[mReviewList.size()];
                        for(int i=0 ; i<mReviewList.size() ; i++){
                            Review r = mReviewList.get(i);
                            contentValues[i] = new ContentValues();
                            contentValues[i].put(MovieContract.ReviewEntry.COLUMN_AUTHOR, r.getAuthor());
                            contentValues[i].put(MovieContract.ReviewEntry.COLUMN_CONTENT, r.getContent());
                            contentValues[i].put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, mMovie.getId());
                        }
                        new InsertReviewsTask().execute(contentValues);
                        return;
                    }
                }
                mIsFavorite = true;
                mMovieDetailView.turnFavorite(true);
            }else{
                if(taskNumber == 1){//selesai delete reviews
                    if(mTrailerList.size() > 0){
                        new DeleteTrailersTask().execute();
                    }else{
                        new DeleteMovieTask().execute();
                    }
                    return;
                }else if(taskNumber == 2){//selesai delete trailers
                    new DeleteMovieTask().execute();
                    return;
                }
                mIsFavorite = false;
                mMovieDetailView.turnFavorite(false);
            }
        }
    };

    @Override
    public void setMovieAsFavorite(Movie movie, List<Trailer> trailerList, List<Review> reviewList) {
        mMovie = movie;
        mTrailerList = trailerList;
        mReviewList = reviewList;
        if(mIsFavorite){
            if(mReviewList.size() > 0){
                new DeleteReviewsTask().execute();
            }else if(mTrailerList.size() > 0){
                new DeleteTrailersTask().execute();
            }else{
                new DeleteMovieTask().execute();
            }

        }else{
            if(mMovie != null){
                ContentValues cv = new ContentValues();
                cv.put(MovieContract.MovieEntry._ID, mMovie.getId());
                cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, mMovie.getOriginalTitle());
                cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
                cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovie.getOriginalPosterPath());
                cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
                cv.put(MovieContract.MovieEntry.COLUMN_USER_RATING, mMovie.getUserRating());
                new InsertMovieTask().execute(cv);
            }
        }
    }
    class InsertMovieTask extends AsyncTask<ContentValues, Void, Boolean>{
        @Override
        protected Boolean doInBackground(ContentValues... params) {
            if(params[0] == null) return false;
            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            Uri returnUri = mContext.getContentResolver().insert(uri, params[0]);
            return returnUri != null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) mCallbackTask.onTaskFinish(1);
        }
    }

    class InsertTrailersTask extends AsyncTask<ContentValues[], Void, Boolean>{

        @Override
        protected Boolean doInBackground(ContentValues[]... params) {
            if(params[0] == null) return false;
            Uri uri = MovieContract.TrailerEntry.CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(mMovie.getId())).build();
            int rowInserted = mContext.getContentResolver().bulkInsert(uri, params[0]);
            return rowInserted > 0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) mCallbackTask.onTaskFinish(2);
        }
    }
    class InsertReviewsTask extends AsyncTask<ContentValues[], Void, Boolean>{
        @Override
        protected Boolean doInBackground(ContentValues[]... params) {
            if(params[0] == null) return false;
            Uri uri = MovieContract.ReviewEntry.CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(mMovie.getId())).build();
            int rowInserted = mContext.getContentResolver().bulkInsert(uri, params[0]);
            return rowInserted > 0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) mCallbackTask.onTaskFinish(3);
        }
    }

    class DeleteReviewsTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            Uri uri = MovieContract.ReviewEntry.buildReviewUri(mMovie.getId());
            int rowDeleted = mContext.getContentResolver().delete(uri, null, null);
            return rowDeleted > 0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) mCallbackTask.onTaskFinish(1);
        }
    }

    class DeleteTrailersTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            Uri uri = MovieContract.TrailerEntry.buildTrailerUri(mMovie.getId());
            int rowDeleted = mContext.getContentResolver().delete(uri, null, null);
            return rowDeleted > 0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) mCallbackTask.onTaskFinish(2);
        }
    }

    class DeleteMovieTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            Uri uri = MovieContract.MovieEntry.buildMovieUri(mMovie.getId());
            int rowDeleted = mContext.getContentResolver().delete(uri, null, null);
            return rowDeleted > 0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) mCallbackTask.onTaskFinish(3);
        }
    }
}
