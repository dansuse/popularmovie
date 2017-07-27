package com.example.danielsetyabudi.movies.data;

import com.example.danielsetyabudi.movies.model.ListModel;
import com.example.danielsetyabudi.movies.model.Movie;
import com.example.danielsetyabudi.movies.model.Review;
import com.example.danielsetyabudi.movies.model.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public interface MoviesServiceApi {
    //interface ini untuk komunikasi antara MoviesServiceApi dengan Repository
    interface MoviesServiceCallback<T> {
        void onLoaded(T results);
    }

    @GET("3/movie/{sort}")
    Call<ListModel<Movie>>loadMovies(@Path("sort") String order, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);

//    @GET("3/movie/popular")
//    Call<ListModel<Movie>>loadPopularMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);
//
//    @GET("3/movie/top_rated")
//    Call<ListModel<Movie>>loadTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);

    @GET("3/movie/{movie_id}/videos")
    Call<ListModel<Trailer>>loadTrailers(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("3/movie/{movie_id}/reviews")
    Call<ListModel<Review>>loadReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

//    void getAllMovies(int mode, MoviesServiceCallback<List<Movie>> callback);

//    void getNextPageMovies(int mode, int page, MoviesServiceCallback<List<Movie>> callback);
}
