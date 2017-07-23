package com.example.danielsetyabudi.movies.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by daniel on 22/07/2017.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.danielsetyabudi.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";
    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_USER_RATING = "userRating";
    }
    public static final class TrailerEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILER)
                .build();
        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }
    public static final class ReviewEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEW)
                .build();
        public static final String TABLE_NAME = "review";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }
}
