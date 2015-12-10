package com.example.evindj.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.evindj.popularmovies.data.*;

/**
 * Created by evindj on 12/8/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME ="movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MovieContract.MovieEntry.getCreateQuery());
        db.execSQL(MovieContract.ReviewEntry.getCreateQuery());
        db.execSQL(MovieContract.TrailerEntry.getCreateQuery());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(MovieContract.MovieEntry.getDropQuery());
        db.execSQL(MovieContract.ReviewEntry.getDropQuery());
        db.execSQL(MovieContract.TrailerEntry.getDropQuery());
        onCreate(db);

    }
}
