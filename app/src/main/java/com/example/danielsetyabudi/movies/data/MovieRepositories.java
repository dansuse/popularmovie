package com.example.danielsetyabudi.movies.data;

import android.support.annotation.NonNull;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public class MovieRepositories {
    private MovieRepositories() {
        // no instance
    }

    private static MoviesRepository repository = null;

    public synchronized static MoviesRepository getInMemoryRepoInstance(@NonNull MoviesServiceApiImpl moviesServiceApi) {
        if (null == repository) {
            repository = new InMemoryMoviesRepository(moviesServiceApi);
        }
        return repository;
    }
}
