package com.example.evindj.popularmovies.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
