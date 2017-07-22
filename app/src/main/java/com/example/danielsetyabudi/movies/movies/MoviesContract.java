package com.example.danielsetyabudi.movies.movies;

import com.example.danielsetyabudi.movies.model.Movie;

import java.util.List;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public interface MoviesContract {
    interface View{
        void showMovies(List<Movie> movies);
        void setProgressIndicator(boolean active);
        void intentToDetail(int movieId, int movieMode);
        void showMessageNoInternetConnection();
        void deleteProgressBarRecyclerView();
    }
    interface UserActionsListener{
        void loadMovies(boolean forceUpdate, int mode);
        void loadMovies(boolean forceUpdate);
        void loadNextPageMovies();
        void handleMovieClicked(int movieId);
    }
}
