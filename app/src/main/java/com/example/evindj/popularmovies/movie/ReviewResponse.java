package com.example.evindj.popularmovies.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by evindj on 12/7/15.
 */
public class ReviewResponse {
    public List<Review> getArray() {
        return array;
    }

    public void setArray(List<Review> array) {
        this.array = array;
    }

    @SerializedName("results")
    List<Review> array;
}
