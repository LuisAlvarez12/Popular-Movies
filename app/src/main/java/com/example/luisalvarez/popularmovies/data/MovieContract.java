package com.example.luisalvarez.popularmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by luisalvarez on 2/11/17.
 */




public class MovieContract {


    public static final String uriAUTHORITY =
            "com.example.luisalvarez.popularmovies";

//    public static final Uri BASE_CONTENT_URI =
//            Uri.parse("content://" +
//                    uriAUTHORITY);

    public static final String sFavorites = "favorites";


    public static final class FavoriteEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final String strContent = "content://com.example.luisalvarez.popularmovies/favorites";
        public static final Uri CONTENT_URI =
                Uri.parse(strContent);

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + sFavorites;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + sFavorites;

        // Define the table schema
        public static final String TABLE_NAME = "favoritesTable";
        public static final String COLUMN_BACKDROP = "movieBackdrop";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_PLOT = "moviePlot";
        public static final String COLUMN_MOVIE_VOTES = "movieVotes";
        public static final String COLUMN_MOVIE_THUMBNAIL = "movieThumbnail";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_GENRES = "movieGenres";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class MovieEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final String strContent = "content://com.example.luisalvarez.popularmovies/favorites";
        public static final Uri CONTENT_URI =
                Uri.parse(strContent);

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + sFavorites;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + sFavorites;

        // Define the table schema
        public static final String TABLE_NAME = "favoritesTable";
        public static final String COLUMN_BACKDROP = "movieBackdrop";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_PLOT = "moviePlot";
        public static final String COLUMN_MOVIE_VOTES = "movieVotes";
        public static final String COLUMN_MOVIE_THUMBNAIL = "movieThumbnail";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_GENRES = "movieGenres";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
