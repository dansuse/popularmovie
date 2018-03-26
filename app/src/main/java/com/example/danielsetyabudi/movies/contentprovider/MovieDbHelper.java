package com.example.danielsetyabudi.movies.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.danielsetyabudi.movies.contentprovider.MovieContract.MovieEntry;
import com.example.danielsetyabudi.movies.contentprovider.MovieContract.ReviewEntry;
import com.example.danielsetyabudi.movies.contentprovider.MovieContract.TrailerEntry;
/**
 * Created by daniel on 22/07/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                        MovieEntry.COLUMN_POSTER_PATH   + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_RELEASE_DATE   + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_USER_RATING   + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        final String SQL_CREATE_TRAILER_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TrailerEntry.TABLE_NAME + " (" +
                        TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                        TrailerEntry.COLUMN_NAME + " TEXT NOT NULL," +
                        TrailerEntry.COLUMN_MOVIE_ID   + " INTEGER NOT NULL, " +
                        "FOREIGN KEY ("+TrailerEntry.COLUMN_MOVIE_ID+") REFERENCES "+MovieEntry.TABLE_NAME+"("+MovieEntry._ID+")" +
                        " );";
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
        final String SQL_CREATE_REVIEW_TABLE =
                "CREATE TABLE IF NOT EXISTS " + ReviewEntry.TABLE_NAME + " (" +
                        ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                        ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL," +
                        ReviewEntry.COLUMN_MOVIE_ID   + " INTEGER NOT NULL, " +
                        "FOREIGN KEY ("+ReviewEntry.COLUMN_MOVIE_ID+") REFERENCES "+MovieEntry.TABLE_NAME+"("+MovieEntry._ID+")" +
                        " );";
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
//        onCreate(db);
    }
}
