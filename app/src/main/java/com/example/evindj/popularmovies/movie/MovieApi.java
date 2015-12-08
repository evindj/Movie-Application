package com.example.evindj.popularmovies.movie;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by evindj on 12/6/15.
 */
public interface MovieApi {

    @GET("/movie/{id}/videos?api_key=Enter your key here")
    public  void getTrailers(@Path("id") Integer id,Callback<TrailerResponse> callback);
    @GET("/movie/{id}/reviews?api_key=Enter your key here")
    public  void getReviews(@Path("id") Integer id,Callback<ReviewResponse> callback);

    }

