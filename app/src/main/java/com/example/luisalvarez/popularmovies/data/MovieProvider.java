package com.example.luisalvarez.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.luisalvarez.popularmovies.data.MovieContract;
import com.example.luisalvarez.popularmovies.data.MovieHelperDb;

/**
 * Created by luisalvarez on 2/11/17.
 */

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = createUriBuilder();
    private MovieHelperDb mOpenHelper;
    private static final int FAV_LIST = 100;
    private static final int FAV_ID = 101;


    @Override
    public boolean onCreate() {
        Log.d("db","1");
        mOpenHelper = new MovieHelperDb(getContext());
        Log.d("db","2");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case FAV_LIST:
                    retCursor = db.query(
                    MovieContract.FavoriteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
                break;
            case FAV_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        MovieContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        MovieContract.FavoriteEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case FAV_LIST:
                return MovieContract.FavoriteEntry.CONTENT_TYPE;
            case FAV_ID:
                return MovieContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " +uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;
                _id = db.insert(MovieContract.FavoriteEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri =  MovieContract.FavoriteEntry.buildMovieUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
        }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows=-1;
        switch (sUriMatcher.match(uri)){
            case FAV_LIST:
                rows = db.delete(MovieContract.FavoriteEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case FAV_ID:
                rows = db.delete(MovieContract.FavoriteEntry.TABLE_NAME,selection,selectionArgs);
                break;
        }
        // Because null could delete all rows:
//        if(selection == null || rows != 0){
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows=-1;
        switch (sUriMatcher.match(uri)){
            case FAV_LIST:
                rows = db.update(MovieContract.FavoriteEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case FAV_ID:
                rows = db.update(MovieContract.FavoriteEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
        }
        // Because null could delete all rows:
//        if(selection == null || rows != 0){
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
        return rows;
    }

    public static UriMatcher createUriBuilder(){
        String content = MovieContract.uriAUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, MovieContract.sFavorites, FAV_LIST);
        matcher.addURI(content, MovieContract.sFavorites + "/#", FAV_ID);
//        matcher.addURI(content, MovieContract.PATH_MOVIE, MOVIE);
//        matcher.addURI(content, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);

        return matcher;
    }
}
