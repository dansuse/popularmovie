package com.example.danielsetyabudi.movies.data;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public class Movie {
    private int id;
    private String title;
    private String originalTitle;
    private String posterPath;
    private String releaseDate;
    private String overview;//atau synopsis
    private String userRating;//vote_average

    String baseURLPosterPath = "http://image.tmdb.org/t/p/w185";

    public Movie(int id, String title, String posterPath, String releaseDate, String overview, String originalTitle, String userRating) {
        this.id = id;
        this.title = title;
        this.posterPath = baseURLPosterPath + posterPath;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.originalTitle = originalTitle;
        this.userRating = userRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getUserRating() {
        return userRating;
    }

}
