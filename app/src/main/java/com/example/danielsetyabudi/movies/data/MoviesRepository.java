package com.example.danielsetyabudi.movies.data;

import android.support.annotation.NonNull;

import com.example.danielsetyabudi.movies.model.Movie;
import com.example.danielsetyabudi.movies.model.Review;
import com.example.danielsetyabudi.movies.model.Trailer;

import java.util.List;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public interface MoviesRepository {

    //ini interface untuk diimplementasikan di presenter
    interface MoviesRepositoryCallback<T> {
        void onResultLoaded(T result);
    }

    void getMovies(int mode, boolean hasInternetConnection, @NonNull MoviesRepositoryCallback<List<Movie>> callback);

    void getNextPageMovies(int mode, @NonNull MoviesRepositoryCallback<List<Movie>> callback);

    void getMovie(int mode, @NonNull int movieId, @NonNull MoviesRepositoryCallback<Movie> callback);

    void getTrailers(int mode, @NonNull int movieId, @NonNull MoviesRepositoryCallback<List<Trailer>> callback);

    void getReviews(int mode, int page, @NonNull int movieId, @NonNull MoviesRepositoryCallback<List<Review>> callback);

    void refreshData(int mode);
}
