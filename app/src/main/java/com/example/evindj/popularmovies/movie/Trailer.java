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
public class Trailer implements Parcelable {
    @SerializedName("id")
    private String id;
    @Expose(serialize = false, deserialize = false)
    private int idMovie;
    @SerializedName("key")
    private String youtubeKey;
    @SerializedName("name")
    private String name;

    public Trailer(String id, int idMovie, String youtubeKey, String name) {
        this.id = id;
        this.idMovie = idMovie;
        this.youtubeKey = youtubeKey;
        this.name = name;
    }

    public Trailer() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }
    public Trailer(Parcel in){
        this.youtubeKey = in.readString();
        this.name= in.readString();
        this.idMovie = in.readInt();
        this.id = in.readString();
    }

    public static ArrayList<Trailer> getTrailers(Context context, int movie){
        String args[] ={Integer.toString(movie)};
        SQLiteDatabase db = new MovieDbHelper(context).getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + MovieContract.TrailerEntry.TABLE_NAME + " Where "
                + MovieContract.TrailerEntry.COLUMN_TRAIL_MOVIE + " = ?", args);
        ArrayList<Trailer> trailers = new ArrayList<>();
        if(c.moveToFirst()) {
            do{
                Trailer trailer = new Trailer();
                trailer.setId(c.getString(c.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAIL_KEY)));
                trailer.setName(c.getString(c.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAIL_NAME)));
                trailer.setYoutubeKey(c.getString(c.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAIL_YOUKEY)));
                trailer.setIdMovie(movie);
                trailers.add(trailer);
            }while(c.moveToNext());
        }
        return trailers;
    }

    public Cursor getTrailers(Context context){
        String args[] ={Integer.toString(getIdMovie())};
        SQLiteDatabase db = new MovieDbHelper(context).getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+MovieContract.TrailerEntry.TABLE_NAME +"Where "
                +MovieContract.TrailerEntry.COLUMN_TRAIL_MOVIE +" = ?",args);
    }
    public void save(Context context){
        SQLiteDatabase db  = new MovieDbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieContract.TrailerEntry.COLUMN_TRAIL_KEY, id);
        values.put(MovieContract.TrailerEntry.COLUMN_TRAIL_NAME, name);
        values.put(MovieContract.TrailerEntry.COLUMN_TRAIL_YOUKEY, youtubeKey);
        values.put(MovieContract.TrailerEntry.COLUMN_TRAIL_MOVIE, idMovie);
        db.insert(MovieContract.TrailerEntry.TABLE_NAME,null,values);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(youtubeKey);
        dest.writeInt(idMovie);
        dest.writeString(id);
    }

    public final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>(){
        @Override
        public Trailer createFromParcel(Parcel parcel){
            return  new Trailer(parcel);
        }
        @Override
        public Trailer[] newArray(int i){
            return new  Trailer[i];
        }
    };
}
