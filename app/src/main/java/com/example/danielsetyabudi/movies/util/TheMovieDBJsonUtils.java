package com.example.danielsetyabudi.movies.util;

import com.example.danielsetyabudi.movies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public final class TheMovieDBJsonUtils {
    public static List<Movie> getSimpleMovieStringsFromJson(String movieJsonStr)
            throws JSONException {

        final String TMDB_RESULTS = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_TITLE = "title";
        final String TMDB_ID = "id";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_ORIGINAL_TITLE = "original_title";
        final String TMDB_VOTE_AVERAGE = "vote_average";


        List<Movie> parsedMovieData = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        if (movieJson.has(TMDB_RESULTS)) {
            JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject item = movieArray.getJSONObject(i);
                String poster_path = item.getString(TMDB_POSTER_PATH);
                String release_date = item.getString(TMDB_RELEASE_DATE);
                String title = item.getString(TMDB_TITLE);
                String overview = item.getString(TMDB_OVERVIEW);
                int id = item.getInt(TMDB_ID);
                String original_title = item.getString(TMDB_ORIGINAL_TITLE);
                String vote_average = item.getString(TMDB_VOTE_AVERAGE);
                parsedMovieData.add(new Movie(id, title, poster_path, release_date, overview, original_title, vote_average));
            }
        }else{
            return null;
        }

        return parsedMovieData;
    }
}
