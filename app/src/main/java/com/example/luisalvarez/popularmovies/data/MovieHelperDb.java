package com.example.luisalvarez.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by luisalvarez on 2/11/17.
 */

public class MovieHelperDb extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "movies.db";

    public MovieHelperDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TOPRATED_TABLE = "CREATE TABLE " + DataContract.TopRatedEntry.TABLE_NAME + " (" +
                DataContract.TopRatedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataContract.TopRatedEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                DataContract.TopRatedEntry.COLUMN_MOVIE_GENRES + " TEXT NOT NULL, " +
                DataContract.TopRatedEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                DataContract.TopRatedEntry.COLUMN_MOVIE_PLOT + " TEXT NOT NULL, " +
                DataContract.TopRatedEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                DataContract.TopRatedEntry.COLUMN_MOVIE_THUMBNAIL + " TEXT NOT NULL, " +
                DataContract.TopRatedEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                DataContract.TopRatedEntry.COLUMN_MOVIE_VOTES + " TEXT NOT NULL," +
                DataContract.TopRatedEntry.COLUMN_DATE_GENERATED + " TEXT NOT NULL"+
                ");";
        final String CREATE_POPULAR_TABLE = "CREATE TABLE " + DataContract.PopularEntry.TABLE_NAME + " (" +
                DataContract.PopularEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataContract.PopularEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                DataContract.PopularEntry.COLUMN_MOVIE_GENRES + " TEXT NOT NULL, " +
                DataContract.PopularEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                DataContract.PopularEntry.COLUMN_MOVIE_PLOT + " TEXT NOT NULL, " +
                DataContract.PopularEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                DataContract.PopularEntry.COLUMN_MOVIE_THUMBNAIL + " TEXT NOT NULL, " +
                DataContract.PopularEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                DataContract.PopularEntry.COLUMN_MOVIE_VOTES + " TEXT NOT NULL," +
                DataContract.PopularEntry.COLUMN_DATE_GENERATED + " TEXT NOT NULL"+
                ");";
        final String CREATE_UPCOMING_TABLE = "CREATE TABLE " + DataContract.UpcomingEntry.TABLE_NAME + " (" +
                DataContract.UpcomingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataContract.UpcomingEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                DataContract.UpcomingEntry.COLUMN_MOVIE_GENRES + " TEXT NOT NULL, " +
                DataContract.UpcomingEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                DataContract.UpcomingEntry.COLUMN_MOVIE_PLOT + " TEXT NOT NULL, " +
                DataContract.UpcomingEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                DataContract.UpcomingEntry.COLUMN_MOVIE_THUMBNAIL + " TEXT NOT NULL, " +
                DataContract.UpcomingEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                DataContract.UpcomingEntry.COLUMN_MOVIE_VOTES + " TEXT NOT NULL," +
                DataContract.UpcomingEntry.COLUMN_DATE_GENERATED + " TEXT NOT NULL"+
                ");";
        final String CREATE_FAVORITES_TABLE = "CREATE TABLE " + DataContract.FavoriteEntry.TABLE_NAME + " (" +
                DataContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataContract.FavoriteEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                DataContract.FavoriteEntry.COLUMN_MOVIE_GENRES + " TEXT NOT NULL, " +
                DataContract.FavoriteEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                DataContract.FavoriteEntry.COLUMN_MOVIE_PLOT + " TEXT NOT NULL, " +
                DataContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                DataContract.FavoriteEntry.COLUMN_MOVIE_THUMBNAIL + " TEXT NOT NULL, " +
                DataContract.FavoriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                DataContract.FavoriteEntry.COLUMN_MOVIE_VOTES + " TEXT NOT NULL," +
                DataContract.FavoriteEntry.COLUMN_DATE_GENERATED + " TEXT NOT NULL"+
                ");";
        db.execSQL(CREATE_FAVORITES_TABLE);
        db.execSQL(CREATE_TOPRATED_TABLE);
        db.execSQL(CREATE_POPULAR_TABLE);
        db.execSQL(CREATE_UPCOMING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.TopRatedEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.UpcomingEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.PopularEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
