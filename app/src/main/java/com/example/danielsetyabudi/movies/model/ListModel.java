package com.example.danielsetyabudi.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by daniel on 22/07/2017.
 */

public class ListModel<T> {
    private List<T> results;

    public List<T> getResults() {
        return results;
    }
}
