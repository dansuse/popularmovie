package com.example.danielsetyabudi.movies.data;

import android.support.annotation.NonNull;

import com.example.danielsetyabudi.movies.BuildConfig;
import com.example.danielsetyabudi.movies.model.ListModel;
import com.example.danielsetyabudi.movies.model.Movie;
import com.example.danielsetyabudi.movies.model.Review;
import com.example.danielsetyabudi.movies.model.Trailer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_POPULAR;
import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_TOP_RATED;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public class MoviesServiceApiImpl
        //implements MoviesServiceApi
{
    private static final String BASE_URL = "https://api.themoviedb.org/";
    private final MoviesServiceApi mMoviesServiceApi;

    public MoviesServiceApiImpl() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mMoviesServiceApi = retrofit.create(MoviesServiceApi.class);
    }

    public void loadMovies(int mode, int page, @NonNull final MoviesServiceApi.MoviesServiceCallback<List<Movie>> callback){
        Call<ListModel<Movie>> call = null;
        if(mode == MODE_POPULAR){
            //call = mMoviesServiceApi.loadPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, "en-US", String.valueOf(page));
            call = mMoviesServiceApi.loadMovies("popular", BuildConfig.THE_MOVIE_DB_API_TOKEN, "en-US", String.valueOf(page));
        }else if(mode == MODE_TOP_RATED){
            //call = mMoviesServiceApi.loadTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, "en-US", String.valueOf(page));
            call = mMoviesServiceApi.loadMovies("top_rated", BuildConfig.THE_MOVIE_DB_API_TOKEN, "en-US", String.valueOf(page));
        }
        if(call != null){
            call.enqueue(new Callback<ListModel<Movie>>() {
                @Override
                public void onResponse(Call<ListModel<Movie>> call, Response<ListModel<Movie>> response) {
                    if(response.isSuccessful()) {
                        List<Movie> moviesList = response.body().getResults();
                        callback.onLoaded(moviesList);
                    } else {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ListModel<Movie>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void loadTrailers(int movieId, @NonNull final MoviesServiceApi.MoviesServiceCallback<List<Trailer>> callback){
        Call<ListModel<Trailer>> call = null;
        call = mMoviesServiceApi.loadTrailers(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN, "en-US");
        if(call != null){
            call.enqueue(new Callback<ListModel<Trailer>>() {
                @Override
                public void onResponse(Call<ListModel<Trailer>> call, Response<ListModel<Trailer>> response) {
                    if(response.isSuccessful()) {
                        List<Trailer> results = response.body().getResults();
                        callback.onLoaded(results);
                    } else {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ListModel<Trailer>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void loadReviews(int page, int movieId, @NonNull final MoviesServiceApi.MoviesServiceCallback<List<Review>> callback){
        Call<ListModel<Review>> call = null;
        call = mMoviesServiceApi.loadReviews(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN, "en-US", page);
        if(call != null){
            call.enqueue(new Callback<ListModel<Review>>() {
                @Override
                public void onResponse(Call<ListModel<Review>> call, Response<ListModel<Review>> response) {
                    if(response.isSuccessful()) {
                        List<Review> results = response.body().getResults();
                        callback.onLoaded(results);
                    } else {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ListModel<Review>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    //    @Override
//    public void getAllMovies(int mode, MoviesServiceCallback<List<Movie>> callback) {
//        URL url = null;
//        if(mode == MODE_POPULAR){
//            url = MoviesServiceApiEndpoint.buildUrl(MoviesServiceApiEndpoint.POPULAR_PATH);
//        }else if(mode == MODE_TOP_RATED){
//            url = MoviesServiceApiEndpoint.buildUrl(MoviesServiceApiEndpoint.TOP_RATED_PATH);
//        }
//        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(callback);
//        fetchMoviesTask.execute(url);
//    }
//
//    @Override
//    public void getNextPageMovies(int mode, int page, MoviesServiceCallback<List<Movie>> callback) {
//        URL url = null;
//        if(mode == MODE_POPULAR){
//            url = MoviesServiceApiEndpoint.buildUrl(MoviesServiceApiEndpoint.POPULAR_PATH, page);
//        }else if(mode == MODE_TOP_RATED){
//            url = MoviesServiceApiEndpoint.buildUrl(MoviesServiceApiEndpoint.TOP_RATED_PATH, page);
//        }
//        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(callback);
//        fetchMoviesTask.execute(url);
//    }

//    class FetchMoviesTask extends AsyncTask<URL, Void, List<Movie>>{
//        MoviesServiceCallback<List<Movie>> callback;
//
//        public FetchMoviesTask(MoviesServiceCallback<List<Movie>> callback) {
//            this.callback = callback;
//        }
//
//        @Override
//        protected List<Movie> doInBackground(URL... params) {
//            if(params.length == 0) return null;
//            URL url = params[0];
//            List<Movie> parsedMovie = null;
//            try{
//                String result = MoviesServiceApiEndpoint.getResponseFromHttpUrl(url);
//                parsedMovie = TheMovieDBJsonUtils.getSimpleMovieStringsFromJson(result);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            return parsedMovie;
//        }
//
//        @Override
//        protected void onPostExecute(List<Movie> s) {
//            if(s != null){
//                callback.onLoaded(s);
//            }
//        }
//    }
}
