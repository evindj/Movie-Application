package com.example.evindj.popularmovies.movie;

import android.os.Parcel;
import android.os.Parcelable;


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

    public void save(){

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
