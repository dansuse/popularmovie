package com.example.danielsetyabudi.movies.data;

import android.support.annotation.NonNull;

import com.example.danielsetyabudi.movies.model.Movie;

import java.util.List;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public interface MoviesRepository {

    interface LoadMoviesCallback {

        void onMoviesLoaded(List<Movie> movies);
    }

    interface GetMovieCallback {

        void onMovieLoaded(Movie movie);
    }

    void getMovies(int mode, boolean hasInternetConnection, @NonNull LoadMoviesCallback callback);

    void getNextPageMovies(int mode, @NonNull LoadMoviesCallback callback);

    void getMovie(int mode, @NonNull int movieId, @NonNull GetMovieCallback callback);

    void refreshData(int mode);
}
