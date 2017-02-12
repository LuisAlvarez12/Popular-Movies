package com.example.luisalvarez.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by luisalvarez on 2/11/17.
 */

public class MovieHelperDb extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fav.db";

    public MovieHelperDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("db","3");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.MovieEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_GENRES + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_PLOT + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_THUMBNAIL + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_VOTES + " TEXT NOT NULL" +
                        ");"
        );
        Log.d("db","4");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
