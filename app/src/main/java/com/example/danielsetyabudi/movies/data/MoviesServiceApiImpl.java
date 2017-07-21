package com.example.danielsetyabudi.movies.data;

import android.os.AsyncTask;

import com.example.danielsetyabudi.movies.util.TheMovieDBJsonUtils;

import java.net.URL;
import java.util.List;

import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_POPULAR;
import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_TOP_RATED;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public class MoviesServiceApiImpl implements MoviesServiceApi {
    @Override
    public void getAllMovies(int mode, MoviesServiceCallback<List<Movie>> callback) {
        URL url = null;
        if(mode == MODE_POPULAR){
            url = MoviesServiceApiEndpoint.buildUrl(MoviesServiceApiEndpoint.POPULAR_PATH);
        }else if(mode == MODE_TOP_RATED){
            url = MoviesServiceApiEndpoint.buildUrl(MoviesServiceApiEndpoint.TOP_RATED_PATH);
        }
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(callback);
        fetchMoviesTask.execute(url);
    }

    @Override
    public void getNextPageMovies(int mode, int page, MoviesServiceCallback<List<Movie>> callback) {
        URL url = null;
        if(mode == MODE_POPULAR){
            url = MoviesServiceApiEndpoint.buildUrl(MoviesServiceApiEndpoint.POPULAR_PATH, page);
        }else if(mode == MODE_TOP_RATED){
            url = MoviesServiceApiEndpoint.buildUrl(MoviesServiceApiEndpoint.TOP_RATED_PATH, page);
        }
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(callback);
        fetchMoviesTask.execute(url);
    }

    class FetchMoviesTask extends AsyncTask<URL, Void, List<Movie>>{
        MoviesServiceCallback<List<Movie>> callback;

        public FetchMoviesTask(MoviesServiceCallback<List<Movie>> callback) {
            this.callback = callback;
        }

        @Override
        protected List<Movie> doInBackground(URL... params) {
            if(params.length == 0) return null;
            URL url = params[0];
            List<Movie> parsedMovie = null;
            try{
                String result = MoviesServiceApiEndpoint.getResponseFromHttpUrl(url);
                parsedMovie = TheMovieDBJsonUtils.getSimpleMovieStringsFromJson(result);
            }catch (Exception e){
                e.printStackTrace();
            }
            return parsedMovie;
        }

        @Override
        protected void onPostExecute(List<Movie> s) {
            if(s != null){
                callback.onLoaded(s);
            }
        }
    }
}
