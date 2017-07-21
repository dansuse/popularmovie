package com.example.danielsetyabudi.movies.movies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.example.danielsetyabudi.movies.data.Movie;
import com.example.danielsetyabudi.movies.data.MoviesRepository;

import java.util.List;

import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_POPULAR;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public class MoviesPresenter implements MoviesContract.UserActionsListener {

    private final MoviesRepository mMoviesRepository;
    private final MoviesContract.View mMoviesView;
    private int mMovieMode = MODE_POPULAR;

    public MoviesPresenter(
            @NonNull MoviesRepository moviesRepository, @NonNull MoviesContract.View moviesView) {
        mMoviesRepository = moviesRepository;
        mMoviesView = moviesView;
    }

    public boolean isOnline() {
        Context context = (MoviesActivity)mMoviesView;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void loadMovies(boolean forceUpdate) {
        loadMovies(forceUpdate, mMovieMode);
    }

    @Override
    public void loadMovies(boolean forceUpdate, int mode) {
        if(isOnline()){
            mMoviesView.setProgressIndicator(true);
            mMovieMode = mode;
            if(forceUpdate){
                mMoviesRepository.refreshData(mMovieMode);
            }
            mMoviesRepository.getMovies(mMovieMode, true, new MoviesRepository.LoadMoviesCallback() {
                @Override
                public void onMoviesLoaded(List<Movie> movies) {
                    mMoviesView.showMovies(movies);
                    mMoviesView.setProgressIndicator(false);
                }
            });
        }else{
            mMoviesView.setProgressIndicator(false);
            mMoviesView.showMessageNoInternetConnection();
            mMovieMode = mode;
            mMoviesRepository.getMovies(mode, false, new MoviesRepository.LoadMoviesCallback() {
                @Override
                public void onMoviesLoaded(List<Movie> movies) {
                    mMoviesView.showMovies(movies);
                }
            });
        }
    }

    @Override
    public void loadNextPageMovies() {
        if(isOnline()){
            mMoviesRepository.getNextPageMovies(mMovieMode, new MoviesRepository.LoadMoviesCallback() {
                @Override
                public void onMoviesLoaded(List<Movie> movies) {
                    mMoviesView.showMovies(movies);
                }
            });
        }else{
            mMoviesView.deleteProgressBarRecyclerView();
            mMoviesView.showMessageNoInternetConnection();
        }
    }

    @Override
    public void handleMovieClicked(int movieId) {
        mMoviesView.intentToDetail(movieId, mMovieMode);
    }
}
