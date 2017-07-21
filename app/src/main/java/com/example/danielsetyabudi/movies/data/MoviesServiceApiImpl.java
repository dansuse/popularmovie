package com.example.danielsetyabudi.movies.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.danielsetyabudi.movies.BuildConfig;
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
        implements Callback<MovieList>
{
    private static final String BASE_URL = "https://api.themoviedb.org/";
    private MoviesServiceApi.MoviesServiceCallback<List<Movie>> mCallback;

    public void loadMovies(int mode, int page, @NonNull MoviesServiceApi.MoviesServiceCallback<List<Movie>> callback){
        mCallback = callback;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MoviesServiceApi moviesServiceApi = retrofit.create(MoviesServiceApi.class);
        Call<MovieList> call = null;
        if(mode == MODE_POPULAR){
            call = moviesServiceApi.loadPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, "en-US", String.valueOf(page));
        }else if(mode == MODE_TOP_RATED){
            call = moviesServiceApi.loadTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, "en-US", String.valueOf(page));
        }
        if(call != null){
            call.enqueue(this);
        }
    }

    @Override
    public void onResponse(@NonNull Call<MovieList> call,@NonNull Response<MovieList> response) {
        if(response.isSuccessful()) {
            List<Movie> moviesList = response.body().getMovies();
            mCallback.onLoaded(moviesList);
        } else {
//            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<MovieList> call, Throwable t) {
//        t.printStackTrace();
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
