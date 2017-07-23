package com.example.danielsetyabudi.movies.moviedetail;

import com.example.danielsetyabudi.movies.model.Movie;
import com.example.danielsetyabudi.movies.model.Review;
import com.example.danielsetyabudi.movies.model.Trailer;

import java.util.List;

/**
 * Created by Daniel Setyabudi on 30/06/2017.
 */

public interface MovieDetailContract {
    interface View{
        void showMovieTitle(String title);
        void hideMovieTitle();

        void showSynopsis(String synopsis);
        void hideSynopsis();

        void showReleaseDate(String releaseDate);
        void hideReleaseDate();

        void showUserRating(String userRating);
        void hideUserRating();

        void showMoviePoster(String imageUrl);
        void hideMoviePoster();

        void setSubtitle(String subtitle);

        void setTitle(String title);

        void showDialogFragmentMoviePoster(String imageUrl);

        void showTrailers(List<Trailer> trailers);
        void showReviews(List<Review> reviews);

        void turnFavorite(boolean favorite);

        void setMovie(Movie movie);
    }
    interface UserActionsListener{
        void openMovie(int mode, int movieId);
        void handleKlikMoviePosterImageView();
        //void checkIfMovieIsFavorite(Context context);
        void checkIfMovieIsFavorite(int movieId);
        void setMovieAsFavorite(Movie movie, List<Trailer> trailerList, List<Review> reviewList);

    }
}
