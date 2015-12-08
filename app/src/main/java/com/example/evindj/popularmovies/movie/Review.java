package com.example.evindj.popularmovies.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by evindj on 12/6/15.
 */
public class Review implements Parcelable {
    @SerializedName("id")
    private String id;
    @Expose(serialize = false, deserialize = false)
    private int movieId;
    @SerializedName("content")
    private String reviewText;

    public Review(String id, int movieId, String reviewText) {
        this.id = id;
        this.movieId = movieId;
        this.reviewText = reviewText;
    }

    public Review() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    public Review(Parcel in){
        this.reviewText= in.readString();
        this.movieId = in.readInt();
        this.id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewText);
        dest.writeInt(movieId);
        dest.writeString(id);
    }

    public final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>(){
        @Override
        public Review createFromParcel(Parcel parcel){
            return  new Review(parcel);
        }
        @Override
        public Review[] newArray(int i){
            return new  Review[i];
        }
    };

}
