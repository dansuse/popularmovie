package com.example.danielsetyabudi.movies.data;

import android.net.Uri;

import com.example.danielsetyabudi.movies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

//this class is not used if we use retrofit
public final class MoviesServiceApiEndpoint {

    private static final String api_key = BuildConfig.THE_MOVIE_DB_API_TOKEN;
    private static final String language = "en-US";
    private static final String page = "1";

    private static final String MOVIEDB_BASE_URL = "https://api.themoviedb.org";

    public static final String POPULAR_PATH = "3/movie/popular";
    public static final String TOP_RATED_PATH = "3/movie/top_rated";


    private static final String API_KEY_PARAM = "api_key";
    private static final String LANGUAGE_PARAM = "language";
    private static final String PAGE_PARAM = "page";

    public static URL buildUrl(String path){
        Uri uri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .path(path)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();
        URL url = null;
        try{
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrl(String path, int page){
        Uri uri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .path(path)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
                .build();
        URL url = null;
        try{
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
