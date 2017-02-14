package com.example.luisalvarez.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by luisalvarez on 2/11/17.
 */

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = createUriBuilder();
    private MovieHelperDb mOpenHelper;
    private static final int FAV_LIST = 100;
    private static final int FAV_ID = 101;
    private static final int TOP_RATED_LIST = 102;
    private static final int TOP_RATED_ID = 103;
    private static final int POPULAR_LIST = 104;
    private static final int POPULAR_ID = 105;
    private static final int UPCOMING_LIST = 106;
    private static final int UPCOMING_ID = 107;


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieHelperDb(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;
        long _id;
        switch (sUriMatcher.match(uri)){
            case UPCOMING_LIST:
                    retCursor = db.query(
                    DataContract.UpcomingEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
                break;
            case TOP_RATED_LIST:
                retCursor = db.query(
                        DataContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case POPULAR_LIST:
                retCursor = db.query(
                        DataContract.PopularEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case UPCOMING_ID:
                 _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        DataContract.UpcomingEntry.TABLE_NAME,
                        projection,
                        DataContract.UpcomingEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case TOP_RATED_ID:
                 _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        DataContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        DataContract.TopRatedEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case POPULAR_ID:
                 _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        DataContract.PopularEntry.TABLE_NAME,
                        projection,
                        DataContract.PopularEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAV_LIST:
                retCursor = db.query(
                        DataContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAV_ID:
                 _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        DataContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        DataContract.FavoriteEntry._ID + " = ?",
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

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case UPCOMING_LIST:
                return DataContract.UpcomingEntry.CONTENT_TYPE;
            case UPCOMING_ID:
                return DataContract.UpcomingEntry.CONTENT_ITEM_TYPE;
            case TOP_RATED_LIST:
                return DataContract.TopRatedEntry.CONTENT_TYPE;
            case TOP_RATED_ID:
                return DataContract.TopRatedEntry.CONTENT_ITEM_TYPE;
            case POPULAR_LIST:
                return DataContract.PopularEntry.CONTENT_TYPE;
            case POPULAR_ID:
                return DataContract.PopularEntry.CONTENT_ITEM_TYPE;
            case FAV_LIST:
                return DataContract.PopularEntry.CONTENT_TYPE;
            case FAV_ID:
                return DataContract.PopularEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " +uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;
        _id = db.insert(DataContract.FavoriteEntry.TABLE_NAME, null, values);
        if(_id > 0){
            returnUri =  DataContract.FavoriteEntry.buildMovieUri(_id);
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
            case UPCOMING_LIST:
                rows = db.delete(DataContract.UpcomingEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case UPCOMING_ID:
                rows = db.delete(DataContract.UpcomingEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case TOP_RATED_LIST:
                rows = db.delete(DataContract.TopRatedEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case TOP_RATED_ID:
                rows = db.delete(DataContract.TopRatedEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case POPULAR_LIST:
                rows = db.delete(DataContract.PopularEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case POPULAR_ID:
                rows = db.delete(DataContract.PopularEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case FAV_ID:
                rows = db.delete(DataContract.FavoriteEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case FAV_LIST:
                rows = db.delete(DataContract.FavoriteEntry.TABLE_NAME,selection,selectionArgs);
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
            case UPCOMING_LIST:
                rows = db.update(DataContract.UpcomingEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case UPCOMING_ID:
                rows = db.update(DataContract.UpcomingEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case TOP_RATED_LIST:
                rows = db.update(DataContract.TopRatedEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case TOP_RATED_ID:
                rows = db.update(DataContract.TopRatedEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case POPULAR_LIST:
                rows = db.update(DataContract.PopularEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case POPULAR_ID:
                rows = db.update(DataContract.PopularEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case FAV_LIST:
                rows = db.update(DataContract.FavoriteEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case FAV_ID:
                rows = db.update(DataContract.FavoriteEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
        }
        // Because null could delete all rows:
//        if(selection == null || rows != 0){
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
        return rows;
    }

    public static UriMatcher createUriBuilder(){
        String content = DataContract.uriAUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, DataContract.sUpcoming, UPCOMING_LIST);
        matcher.addURI(content, DataContract.sUpcoming + "/#", UPCOMING_ID);
        matcher.addURI(content, DataContract.sTopRated, TOP_RATED_LIST);
        matcher.addURI(content, DataContract.sTopRated, TOP_RATED_LIST);
        matcher.addURI(content, DataContract.sTopRated + "/#", TOP_RATED_ID);
        matcher.addURI(content, DataContract.sPopular, POPULAR_LIST);
        matcher.addURI(content, DataContract.sPopular + "/#", POPULAR_ID);
        matcher.addURI(content, DataContract.sFavorites, FAV_LIST);
        matcher.addURI(content, DataContract.sFavorites + "/#", FAV_ID);


//        matcher.addURI(content, DataContract.PATH_MOVIE, MOVIE);
//        matcher.addURI(content, DataContract.PATH_MOVIE + "/#", MOVIE_ID);
        return matcher;
    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;
        switch (match) {
            case TOP_RATED_LIST:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.TopRatedEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case POPULAR_LIST:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.PopularEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case UPCOMING_LIST:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.UpcomingEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case FAV_LIST:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.FavoriteEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
