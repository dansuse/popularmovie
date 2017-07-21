package com.example.danielsetyabudi.movies.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public interface MoviesServiceApi {
    interface MoviesServiceCallback<T> {
        void onLoaded(T movies);
    }

    @GET("3/movie/popular")
    Call<MovieList>loadPopularMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);

    @GET("3/movie/top_rated")
    Call<MovieList>loadTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);

//    void getAllMovies(int mode, MoviesServiceCallback<List<Movie>> callback);

//    void getNextPageMovies(int mode, int page, MoviesServiceCallback<List<Movie>> callback);
}
