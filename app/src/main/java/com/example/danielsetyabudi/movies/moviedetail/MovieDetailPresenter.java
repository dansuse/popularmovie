package com.example.danielsetyabudi.movies.moviedetail;

import android.support.annotation.NonNull;

import com.example.danielsetyabudi.movies.data.Movie;
import com.example.danielsetyabudi.movies.data.MoviesRepository;

/**
 * Created by Daniel Setyabudi on 30/06/2017.
 */

public class MovieDetailPresenter implements MovieDetailContract.UserActionsListener {

    private final MoviesRepository mMoviesRepository;
    private final MovieDetailContract.View mMovieDetailView;
    private Movie mMovie = null;

    public MovieDetailPresenter(@NonNull MoviesRepository moviesRepository, @NonNull MovieDetailContract.View movieDetailView) {
        mMoviesRepository = moviesRepository;
        mMovieDetailView = movieDetailView;
    }

    @Override
    public void openMovie(int mode, int movieId) {
        mMoviesRepository.getMovie(mode, movieId, new MoviesRepository.GetMovieCallback() {
            @Override
            public void onMovieLoaded(Movie movie) {
                mMovie = movie;
                if(movie == null){

                }else{
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
    }

    @Override
    public void handleKlikMoviePosterImageView() {
        if(mMovie != null){
            mMovieDetailView.showDialogFragmentMoviePoster(mMovie.getPosterPath());
        }
    }
}
