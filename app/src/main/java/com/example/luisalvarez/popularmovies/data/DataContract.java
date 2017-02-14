package com.example.luisalvarez.popularmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by luisalvarez on 2/11/17.
 */




public class DataContract {


    public static final String uriAUTHORITY =
            "com.example.luisalvarez.popularmovies";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" +
                    uriAUTHORITY);

    public static final String sFavorites = "/favorites";
    public static final String sUpcoming = "/upcoming";
    public static final String sTopRated = "/toprated";
    public static final String sPopular = "/popular";


    public static final class FavoriteEntry implements BaseColumns {

        // Content URI represents the base location for the table
        public static final String strContent = BASE_CONTENT_URI+sFavorites;
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
        public static final String COLUMN_DATE_GENERATED = "dateGenerated";


        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class TopRatedEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final String strContent = BASE_CONTENT_URI+sTopRated;
        public static final Uri CONTENT_URI =
                Uri.parse(strContent);

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + sTopRated;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + sTopRated;

        // Define the table schema
        public static final String TABLE_NAME = "topratedTable";
        public static final String COLUMN_BACKDROP = "movieBackdrop";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_PLOT = "moviePlot";
        public static final String COLUMN_MOVIE_VOTES = "movieVotes";
        public static final String COLUMN_MOVIE_THUMBNAIL = "movieThumbnail";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_GENRES = "movieGenres";
        public static final String COLUMN_DATE_GENERATED = "dateGenerated";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static final class PopularEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final String strContent = BASE_CONTENT_URI+sPopular;
        public static final Uri CONTENT_URI =
                Uri.parse(strContent);

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + sPopular;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + sPopular;

        // Define the table schema
        public static final String TABLE_NAME = "popularTable";
        public static final String COLUMN_BACKDROP = "movieBackdrop";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_PLOT = "moviePlot";
        public static final String COLUMN_MOVIE_VOTES = "movieVotes";
        public static final String COLUMN_MOVIE_THUMBNAIL = "movieThumbnail";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_GENRES = "movieGenres";
        public static final String COLUMN_DATE_GENERATED = "dateGenerated";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class UpcomingEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final String strContent = BASE_CONTENT_URI+sUpcoming;
        public static final Uri CONTENT_URI =
                Uri.parse(strContent);

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + sUpcoming;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + sUpcoming;

        // Define the table schema
        public static final String TABLE_NAME = "upcomingTable";
        public static final String COLUMN_BACKDROP = "movieBackdrop";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_PLOT = "moviePlot";
        public static final String COLUMN_MOVIE_VOTES = "movieVotes";
        public static final String COLUMN_MOVIE_THUMBNAIL = "movieThumbnail";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_GENRES = "movieGenres";
        public static final String COLUMN_DATE_GENERATED = "dateGenerated";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
