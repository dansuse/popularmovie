package com.example.danielsetyabudi.movies.util;

import android.database.Cursor;

import com.example.danielsetyabudi.movies.contentprovider.MovieContract;
import com.example.danielsetyabudi.movies.model.Review;
import com.example.danielsetyabudi.movies.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 23/07/2017.
 */

public class CursorToList {

    public static List<Trailer> convertCursorToTrailerList(Cursor cursor){
        List<Trailer>trailerList = new ArrayList<>();
        if(cursor != null && cursor.moveToFirst()){
            do{
                Trailer trailer = new Trailer();
                trailer.setKey(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_KEY)));
                trailer.setName(cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_NAME)));
                trailerList.add(trailer);
            }while(cursor.moveToNext());
        }
        return trailerList;
    }

    public static List<Review> convertCursorToReviewList(Cursor cursor){
        List<Review>reviewList = new ArrayList<>();
        if(cursor != null && cursor.moveToFirst()){
            do{
                Review review = new Review();
                review.setContent(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT)));
                review.setAuthor(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR)));
                reviewList.add(review);
            }while(cursor.moveToNext());
        }
        return reviewList;
    }
}
