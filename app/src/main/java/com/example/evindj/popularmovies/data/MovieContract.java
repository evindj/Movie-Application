package com.example.evindj.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by evindj on 12/8/15.
 */
public class MovieContract {
    public static   class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOV_KEY ="movie_id";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_MOV_PLOT = "plot";
        public static final String COLUMN_MOV_RAT = "rating";
        public static final String COLUMN_MOV_TITLE = "title";
        public static final String COLUMN_MOV_RELDATE = "releasedate";
        public static final String getCreateQuery() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("+
                    COLUMN_MOV_KEY + " INTEGER  UNIQUE, "+
                    COLUMN_MOV_PLOT + " TEXT , " +
                    COLUMN_MOV_RAT + " TEXT , " +
                    COLUMN_MOV_TITLE + " TEXT , " +
                    COLUMN_MOV_RELDATE + " TEXT , " +
            COLUMN_THUMBNAIL + " TEXT , PRIMARY KEY("+ COLUMN_MOV_KEY +") )";
        }
        public static  final  String getDropQuery(){
            return "DROP TABLE IF EXISTS "+ TABLE_NAME;
        }
    }
    public static  class TrailerEntry implements BaseColumns{
        public static final String TABLE_NAME ="trailer";
        public static final String COLUMN_TRAIL_KEY ="trailer_id";
        public static final String COLUMN_TRAIL_NAME ="trailer_name";
        public static final String COLUMN_TRAIL_MOVIE ="movie_id";
        public static final String COLUMN_TRAIL_YOUKEY ="key";
        public static final String getCreateQuery() {return  "CREATE TABLE IF NOT EXISTS  " + TABLE_NAME + " ("+
            COLUMN_TRAIL_KEY + " TEXT UNIQUE, "+
                COLUMN_TRAIL_NAME + " TEXT ,"  +
            COLUMN_TRAIL_YOUKEY + " TEXT ,"+
                COLUMN_TRAIL_MOVIE +  " INTEGER, FOREIGN KEY("+COLUMN_TRAIL_MOVIE+") REFERENCES movie(movie_id),  " +
                "PRIMARY KEY("+ COLUMN_TRAIL_KEY + "))";
        }
        public static  final  String getDropQuery(){
            return "DROP TABLE IF EXISTS "+ TABLE_NAME;
        }
    }
    public static  class ReviewEntry implements BaseColumns{
        public  static final String TABLE_NAME = "review";
        public static final String COLUMN_REV_KEY ="review_id";
        public static final String COLUMN_REV_TEXT ="rev_text";
        public static final String COLUMN_REV_AUTHOR ="rev_author";
        public static final String COLUMN_REV_MOVIE ="movie_id";
        public static final String getCreateQuery() {
            return  "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("+
                COLUMN_REV_KEY + " TEXT UNIQUE,"+
                COLUMN_REV_AUTHOR + " TEXT , " +
                COLUMN_REV_TEXT + " TEXT , "+ COLUMN_REV_MOVIE + " INTEGER, FOREIGN KEY("+COLUMN_REV_MOVIE+") REFERENCES movie(movie_id), "
                 + "PRIMARY KEY("+ COLUMN_REV_KEY   +"))";}
        public static  final  String getDropQuery(){
            return "DROP TABLE IF EXISTS "+ TABLE_NAME;
        }
    }
}
