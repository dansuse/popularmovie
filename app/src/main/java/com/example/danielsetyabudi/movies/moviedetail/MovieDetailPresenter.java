package com.example.danielsetyabudi.movies.moviedetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.danielsetyabudi.movies.model.Movie;
import com.example.danielsetyabudi.movies.data.MoviesRepository;
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

    public MovieDetailPresenter(@NonNull MoviesRepository moviesRepository, @NonNull MovieDetailContract.View movieDetailView) {
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

            }
        });
        mMoviesRepository.getReviews(mode, 1, movieId, new MoviesRepository.MoviesRepositoryCallback<List<Review>>() {
            @Override
            public void onResultLoaded(List<Review> result) {

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
