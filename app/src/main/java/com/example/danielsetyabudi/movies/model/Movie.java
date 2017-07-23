package com.example.danielsetyabudi.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Daniel Setyabudi on 27/06/2017.
 */

public class Movie {
    private int id;
    private String title;

    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    private String overview;//atau synopsis

    @SerializedName("vote_average")
    private String userRating;//vote_average

    private List<Trailer> mTrailerList;
    private List<Review> mReviewList;
    //variabel baseURLPosterPath akan null apabila object movie digenerate oleh
    //gson
//    String baseURLPosterPath = "http://image.tmdb.org/t/p/w185";
//
//    public Movie(int id, String title, String posterPath, String releaseDate, String overview, String originalTitle, String userRating) {
//        this.id = id;
//        this.title = title;
//        this.posterPath = baseURLPosterPath + posterPath;
//        this.releaseDate = releaseDate;
//        this.overview = overview;
//        this.originalTitle = originalTitle;
//        this.userRating = userRating;
//    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return "http://image.tmdb.org/t/p/w185" + posterPath;
    }

    public String getOriginalPosterPath(){
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

    public List<Trailer> getTrailerList() {
        return mTrailerList;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        mTrailerList = trailerList;
    }

    public List<Review> getReviewList() {
        return mReviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        mReviewList = reviewList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }
}
