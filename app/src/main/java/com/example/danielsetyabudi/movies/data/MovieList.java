package com.example.danielsetyabudi.movies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by daniel on 21/07/2017.
 */

public class MovieList {
    @SerializedName("results")
    private List<Movie>movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
