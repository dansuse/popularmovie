package com.example.danielsetyabudi.movies.data;

import android.support.annotation.NonNull;

import com.example.danielsetyabudi.movies.model.Movie;
import com.example.danielsetyabudi.movies.model.Review;
import com.example.danielsetyabudi.movies.model.Trailer;
import com.google.common.collect.Lists;

import java.util.List;

import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_POPULAR;
import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_TOP_RATED;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public class InMemoryMoviesRepository implements MoviesRepository {
    //private final MoviesServiceApi mMoviesServiceApi;
    private final MoviesServiceApiImpl mMoviesServiceApi;

    private int page = 1;
    List<Movie> mCachedPopularMovies = null;
    List<Movie> mCachedTopRatedMovies = null;

    public InMemoryMoviesRepository(@NonNull MoviesServiceApiImpl moviesServiceApi) {
        mMoviesServiceApi = moviesServiceApi;
    }

    @Override
    public void refreshData(int mode) {
        if(mode == MODE_POPULAR){
            mCachedPopularMovies = null;
        }else if(mode == MODE_TOP_RATED){
            mCachedTopRatedMovies = null;
        }
    }

    @Override
    public void getMovies(final int mode, boolean hasInternetConnection, @NonNull final MoviesRepositoryCallback<List<Movie>> callback) {
        // Load from API only if needed.
        if(mode == MODE_POPULAR){
            if (mCachedPopularMovies != null) {
                callback.onResultLoaded(Lists.newArrayList(mCachedPopularMovies));
                return;
            }
        }else if(mode == MODE_TOP_RATED){
            if (mCachedTopRatedMovies != null) {
                callback.onResultLoaded(Lists.newArrayList(mCachedTopRatedMovies));
                return;
            }
        }
        if(hasInternetConnection){
            page = 1;
            mMoviesServiceApi.loadMovies(mode, page, new MoviesServiceApi.MoviesServiceCallback<List<Movie>>() {
                @Override
                public void onLoaded(List<Movie> movies) {
                    //mCachedMovies = ImmutableList.copyOf(movies);
                    if(mode == MODE_POPULAR)
                        mCachedPopularMovies = movies;
                    else if(mode == MODE_TOP_RATED)
                        mCachedTopRatedMovies = movies;
                    callback.onResultLoaded(Lists.newArrayList(movies));
                }
            });
        }else{
            callback.onResultLoaded(null);
        }
    }

    @Override
    public void getNextPageMovies(final int mode, @NonNull final MoviesRepositoryCallback<List<Movie>> callback) {
        page = page + 1;
        mMoviesServiceApi.loadMovies(mode, page, new MoviesServiceApi.MoviesServiceCallback<List<Movie>>() {
            @Override
            public void onLoaded(List<Movie> movies) {
                if(mode == MODE_POPULAR){
                    mCachedPopularMovies.addAll(movies);
                    callback.onResultLoaded(Lists.newArrayList(mCachedPopularMovies));
                }else if(mode == MODE_TOP_RATED){
                    mCachedTopRatedMovies.addAll(movies);
                    callback.onResultLoaded(Lists.newArrayList(mCachedTopRatedMovies));
                }
            }
        });
    }

    @Override
    public void getMovie(int mode, @NonNull int movieId, @NonNull MoviesRepositoryCallback<Movie> callback) {
        List<Movie> temp = getCachedMovies(mode);
        if(temp != null){
            Movie movie = getMovieById(movieId, temp);
            callback.onResultLoaded(movie);
        }
    }

    @Override
    public void getTrailers(int mode, @NonNull int movieId, @NonNull final MoviesRepositoryCallback<List<Trailer>> callback) {
        List<Movie> temp = getCachedMovies(mode);
        if(temp != null){
            final Movie movie = getMovieById(movieId, temp);
            if(movie.getTrailerList() != null){
                callback.onResultLoaded(movie.getTrailerList());
            }else{
                mMoviesServiceApi.loadTrailers(movieId, new MoviesServiceApi.MoviesServiceCallback<List<Trailer>>() {
                    @Override
                    public void onLoaded(List<Trailer> results) {
                        movie.setTrailerList(results);
                        callback.onResultLoaded(results);
                    }
                });
            }
        }
    }

    @Override
    public void getReviews(int mode, int page, @NonNull int movieId, @NonNull final MoviesRepositoryCallback<List<Review>> callback) {
        List<Movie> temp = getCachedMovies(mode);
        if(temp != null){
            final Movie movie = getMovieById(movieId, temp);
            if(movie.getReviewList() != null){
                callback.onResultLoaded(movie.getReviewList());
            }else{
                mMoviesServiceApi.loadReviews(page, movieId, new MoviesServiceApi.MoviesServiceCallback<List<Review>>() {
                    @Override
                    public void onLoaded(List<Review> results) {
                        movie.setReviewList(results);
                        callback.onResultLoaded(results);
                    }
                });
            }
        }
    }

    private List<Movie> getCachedMovies(int mode){
        if(mode == MODE_POPULAR){
            return mCachedPopularMovies;
        }else if(mode == MODE_TOP_RATED){
            return mCachedTopRatedMovies;
        }
        return null;
    }

    private Movie getMovieById(int id, List<Movie>temp){
        for (Movie m : temp) {
            if(m.getId() == id){
                return m;
            }
        }
        return null;
    }

//    @Override
//    public void getFavoriteMovie(@NonNull MoviesRepositoryCallback<List<Movie>> callback) {
//
//    }
//
//    @Override
//    public void checkIfMovieIsFavorite(int movieId, @NonNull MoviesRepositoryCallback<Boolean> callback) {
//        new AsyncTask<Integer, Void, Boolean>(){
//            @Override
//            protected Boolean doInBackground(Integer... params) {
//                if(params[0] == null) return false;
//                boolean isFavorite = false;
//
//                return isFavorite;
//            }
//        }.execute(movieId);
//    }
//
//    @Override
//    public void setMovieAsFavorite(Movie movie, List<Trailer> trailerList, List<Review> reviewList) {
//
//    }
}
