package com.example.evindj.popularmovies.movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import com.example.evindj.popularmovies.data.*;
import com.facebook.stetho.common.ArrayListAccumulator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by evindj on 12/3/15.
 */
public class Movie implements Parcelable {
    public static SortOrder SORTORDER= SortOrder.HIGHESTRATED;
    private String title;
    private String thumbnail;
    private String plotAnalysis;
    private int rating;
    private String releaseDate;
    private int id;
    public Movie(){

    }
    public Movie(String title, String thumbnail, String plotAnalysis, int rating, String releaseDate, int i){
        this.id = i;
        this.releaseDate= releaseDate;
        this.rating= rating;
        this.title= title;
        this.plotAnalysis = plotAnalysis;
        this.thumbnail= thumbnail;

    }
    public Movie(Parcel in){
        this.title = in.readString();
        this.thumbnail= in.readString();
        this.plotAnalysis= in.readString();
        this.rating = in.readInt();
        this.releaseDate= in.readString();
        this.id = in.readInt();
    }

    public int getId(){
        return id;
    }
    public void setId(int i){
        this.id= i;
    }
    public String getReleaseDate(){
        return  releaseDate;
    }
    public void setReleaseDate(String releaseDate){
        this.releaseDate= releaseDate;
    }
    public String getTitle(){
        return  title;
    }
    public void setTitle(String title){
        this.title= title;
    }
    public String getThumbnail(){
        return  thumbnail;
    }
    public void setThumbnail(String thumbnail){
        this.thumbnail= thumbnail;
    }
    public String getPlotAnalysis(){
        return  plotAnalysis;
    }
    public void setPlotAnalysis(String plotAnalysis){
        this.plotAnalysis= plotAnalysis;
    }
    public int getRating(){
        return  rating;
    }
    public void setRating(int rating){
        this.rating= rating;
    }

    public String getCompleteUrl(){
        return  "http://image.tmdb.org/t/p/w185/" + getThumbnail();
    }

    public void save(Context context){
        SQLiteDatabase db  = new MovieDbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOV_KEY, id);
        values.put(MovieContract.MovieEntry.COLUMN_THUMBNAIL, thumbnail);
        values.put(MovieContract.MovieEntry.COLUMN_MOV_PLOT, plotAnalysis);
        values.put(MovieContract.MovieEntry.COLUMN_MOV_RAT, rating);
        values.put(MovieContract.MovieEntry.COLUMN_MOV_TITLE, title);
        values.put(MovieContract.MovieEntry.COLUMN_MOV_RELDATE, releaseDate);
        db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);

    }

    public static ArrayList<Movie> getMovies(Context context)
    {
        SQLiteDatabase db = new MovieDbHelper(context).getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME, null);
        ArrayList<Movie> movies = new ArrayListAccumulator<>();
        if(c.moveToFirst()) {
            do{
            Movie movie = new Movie();
            movie.setId(c.getInt(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOV_KEY)));
            movie.setThumbnail(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_THUMBNAIL)));
                movie.setTitle(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOV_TITLE)));
                movie.setRating(c.getInt(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOV_RAT)));
                movie.setPlotAnalysis(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOV_PLOT)));
                movie.setReleaseDate(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOV_RELDATE)));
            movies.add(movie);
        }while(c.moveToNext());
        }
        return movies;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(thumbnail);
        dest.writeString(plotAnalysis);
        dest.writeInt(rating);
        dest.writeString(releaseDate);
        dest.writeInt(id);
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
         public Movie createFromParcel(Parcel parcel){
            return  new Movie(parcel);
         }
        @Override
        public Movie[] newArray(int i){
            return new  Movie[i];
        }
    };
}
