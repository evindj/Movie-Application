package com.example.evindj.popularmovies.movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.evindj.popularmovies.data.MovieContract;
import com.example.evindj.popularmovies.data.MovieDbHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Cursor getReviews(Context context){
        String args[] ={Integer.toString(getMovieId())};
        SQLiteDatabase db = new MovieDbHelper(context).getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+MovieContract.ReviewEntry.TABLE_NAME +" Where "
                +MovieContract.ReviewEntry.COLUMN_REV_MOVIE +" = ?",args);
    }
    public static ArrayList<Review> getReviews(Context context,int movieId){
        String args[] ={Integer.toString(movieId)};
        SQLiteDatabase db = new MovieDbHelper(context).getReadableDatabase();
       Cursor c = db.rawQuery("SELECT * FROM " + MovieContract.ReviewEntry.TABLE_NAME + " Where "
               + MovieContract.ReviewEntry.COLUMN_REV_MOVIE + " = ?", args);
        ArrayList<Review> reviews = new ArrayList<>();
        if(c.moveToFirst()) {
            do{
                Review review = new Review();
                review.setId(c.getString(c.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REV_KEY)));
                review.setAuthor(c.getString(c.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REV_AUTHOR)));
                review.setMovieId(movieId);
                review.setReviewText(c.getString(c.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REV_TEXT)));
                reviews.add(review);
            }while(c.moveToNext());
        }
        return reviews;
    }
    @SerializedName("author")
    private String author;

    public Review(String id, int movieId, String reviewText,String author) {
        this.id = id;
        this.movieId = movieId;
        this.reviewText = reviewText;
        this.author = author;
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
    public void save(Context context){
        SQLiteDatabase db  = new MovieDbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieContract.ReviewEntry.COLUMN_REV_KEY, id);
        values.put(MovieContract.ReviewEntry.COLUMN_REV_TEXT, reviewText);
        values.put(MovieContract.ReviewEntry.COLUMN_REV_AUTHOR, author);
        values.put(MovieContract.ReviewEntry.COLUMN_REV_MOVIE, movieId);
        db.insert(MovieContract.ReviewEntry.TABLE_NAME,null,values);
    }
    public Review(Parcel in){
        this.reviewText= in.readString();
        this.movieId = in.readInt();
        this.id = in.readString();
        this.author = in.readString();
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
        dest.writeString(author);
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
