package com.example.danielsetyabudi.movies.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.danielsetyabudi.movies.contentprovider.MovieContract.MovieEntry;
import com.example.danielsetyabudi.movies.contentprovider.MovieContract.ReviewEntry;
import com.example.danielsetyabudi.movies.contentprovider.MovieContract.TrailerEntry;

/**
 * Created by daniel on 22/07/2017.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;
    public static final int CODE_TRAILER_BY_MOVIE_ID = 200;
    public static final int CODE_REVIEW_BY_MOVIE_ID = 300;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/#", CODE_TRAILER_BY_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", CODE_REVIEW_BY_MOVIE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mMovieDbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (match){
            case CODE_MOVIE:
                cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            case CODE_MOVIE_WITH_ID:
                //mengecek apakah movie dengan id tertentu termasuk dalam daftar favorite
                String movieId = uri.getPathSegments().get(1);
                String mSelection = "_id = ?";
                String[] mSelectionArgs = new String[]{movieId};
                cursor = database.query(MovieEntry.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            case CODE_REVIEW_BY_MOVIE_ID:
                movieId = uri.getPathSegments().get(1);
                mSelection = "movieId = ?";
                mSelectionArgs = new String[]{movieId};
                cursor = database.query(ReviewEntry.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            case CODE_TRAILER_BY_MOVIE_ID:
                movieId = uri.getPathSegments().get(1);
                mSelection = "movieId = ?";
                mSelectionArgs = new String[]{movieId};
                cursor = database.query(TrailerEntry.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("URI not recognized");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_REVIEW_BY_MOVIE_ID:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    //getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_TRAILER_BY_MOVIE_ID:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    //getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
        long id;
        Uri returnUri = null;
        switch (match){
            case CODE_MOVIE:
                id = database.insert(MovieEntry.TABLE_NAME, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:
                throw new UnsupportedOperationException("URI not recognized");
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (match){
            case CODE_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                rowsDeleted = db.delete(MovieEntry.TABLE_NAME, mSelection, mSelectionArgs);
                if(rowsDeleted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            case CODE_REVIEW_BY_MOVIE_ID:
                String movieId = uri.getPathSegments().get(1);
                mSelection = "movieId=?";
                mSelectionArgs = new String[]{movieId};
                rowsDeleted = db.delete(ReviewEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            case CODE_TRAILER_BY_MOVIE_ID:
                movieId = uri.getPathSegments().get(1);
                mSelection = "movieId=?";
                mSelectionArgs = new String[]{movieId};
                rowsDeleted = db.delete(TrailerEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("URI not recognized");
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
