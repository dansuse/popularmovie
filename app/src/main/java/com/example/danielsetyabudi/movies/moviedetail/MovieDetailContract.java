package com.example.danielsetyabudi.movies.moviedetail;

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

    }
    interface UserActionsListener{
        void openMovie(int mode, int movieId);
        void handleKlikMoviePosterImageView();
    }
}
