package com.example.danielsetyabudi.movies.model;

/**
 * Created by daniel on 22/07/2017.
 */

public class Review {
    private String id;
    private String author;
    private String content;
    private String url;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
