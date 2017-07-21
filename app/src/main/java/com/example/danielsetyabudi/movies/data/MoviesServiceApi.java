package com.example.danielsetyabudi.movies.data;

import java.util.List;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public interface MoviesServiceApi {
    interface MoviesServiceCallback<T> {
        void onLoaded(T notes);
    }

    void getAllMovies(int mode, MoviesServiceCallback<List<Movie>> callback);

    void getNextPageMovies(int mode, int page, MoviesServiceCallback<List<Movie>> callback);
}
