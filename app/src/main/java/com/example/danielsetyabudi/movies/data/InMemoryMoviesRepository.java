package com.example.danielsetyabudi.movies.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.List;

import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_POPULAR;
import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_TOP_RATED;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public class InMemoryMoviesRepository implements MoviesRepository {
    private final MoviesServiceApi mMoviesServiceApi;

    private int page = 1;
    List<Movie> mCachedPopularMovies = null;
    List<Movie> mCachedTopRatedMovies = null;

    public InMemoryMoviesRepository(@NonNull MoviesServiceApi moviesServiceApi) {
        mMoviesServiceApi = moviesServiceApi;
    }

    @Override
    public void getMovies(int mode, boolean hasInternetConnection, @NonNull final LoadMoviesCallback callback) {
        // Load from API only if needed.
        if(mode == MODE_POPULAR){
            if (mCachedPopularMovies == null) {
                if(hasInternetConnection){
                    page = 1;
                    mMoviesServiceApi.getAllMovies(mode, new MoviesServiceApi.MoviesServiceCallback<List<Movie>>() {
                        @Override
                        public void onLoaded(List<Movie> movies) {
                            //mCachedMovies = ImmutableList.copyOf(movies);
                            mCachedPopularMovies = movies;
                            callback.onMoviesLoaded(Lists.newArrayList(mCachedPopularMovies));
                        }
                    });
                }else{
                    callback.onMoviesLoaded(null);
                }
            } else {
                callback.onMoviesLoaded(Lists.newArrayList(mCachedPopularMovies));
            }
        }else if(mode == MODE_TOP_RATED){
            if (mCachedTopRatedMovies == null) {
                if(hasInternetConnection){
                    page = 1;
                    mMoviesServiceApi.getAllMovies(mode, new MoviesServiceApi.MoviesServiceCallback<List<Movie>>() {
                        @Override
                        public void onLoaded(List<Movie> movies) {
                            //mCachedMovies = ImmutableList.copyOf(movies);
                            mCachedTopRatedMovies = movies;
                            callback.onMoviesLoaded(Lists.newArrayList(mCachedTopRatedMovies));
                        }
                    });
                }else{
                    callback.onMoviesLoaded(null);
                }
            } else {
                callback.onMoviesLoaded(Lists.newArrayList(mCachedTopRatedMovies));
            }
        }

    }

    @Override
    public void getNextPageMovies(final int mode, @NonNull final LoadMoviesCallback callback) {
        page = page + 1;
        mMoviesServiceApi.getNextPageMovies(mode, page, new MoviesServiceApi.MoviesServiceCallback<List<Movie>>() {
            @Override
            public void onLoaded(List<Movie> movies) {
                if(mode == MODE_POPULAR){
                    mCachedPopularMovies.addAll(movies);
                    callback.onMoviesLoaded(Lists.newArrayList(mCachedPopularMovies));
                }else if(mode == MODE_TOP_RATED){
                    mCachedTopRatedMovies.addAll(movies);
                    callback.onMoviesLoaded(Lists.newArrayList(mCachedTopRatedMovies));
                }
            }
        });
    }

    @Override
    public void getMovie(int mode, @NonNull int movieId, @NonNull GetMovieCallback callback) {
        List<Movie> temp = null;
        if(mode == MODE_POPULAR){
            temp = mCachedPopularMovies;
        }else if(mode == MODE_TOP_RATED){
            temp = mCachedTopRatedMovies;
        }
        if(temp != null){
            Movie movie = null;
            for (Movie m : temp) {
                if(m.getId() == movieId){
                    movie = m;
                    break;
                }
            }
            callback.onMovieLoaded(movie);
        }
    }

    @Override
    public void refreshData(int mode) {
        if(mode == MODE_POPULAR){
            mCachedPopularMovies = null;
        }else if(mode == MODE_TOP_RATED){
            mCachedTopRatedMovies = null;
        }
    }
}
