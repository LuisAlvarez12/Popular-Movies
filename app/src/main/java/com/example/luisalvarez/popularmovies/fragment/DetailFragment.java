package com.example.luisalvarez.popularmovies.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luisalvarez.popularmovies.CastCrewActivity;
import com.example.luisalvarez.popularmovies.R;
import com.example.luisalvarez.popularmovies.data.DataContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.name;

/**
 * Created by luisalvarez on 1/16/17.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks{
    //image URL start
    public static final String[] mProjectionMovieId = {
            DataContract.FavoriteEntry.COLUMN_MOVIE_ID,
            DataContract.FavoriteEntry._ID
    };
    private int FAVORITES_LOADER = 0;
    private ImageView img_backdrop,img_poster,img_videoThumbnail;
    private TextView tv_plot_overview,tv_title,tv_vote_average,tv_release,tv_genres,tv_cast,tv_videoThumbnailTitle;
    private ImageButton button_fav;
    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";
    private Cursor movieInDatabase;
    public static final String[] mProjection = {
            DataContract.PopularEntry.COLUMN_MOVIE_TITLE,
            DataContract.PopularEntry.COLUMN_MOVIE_PLOT,
            DataContract.PopularEntry.COLUMN_MOVIE_RELEASE_DATE,
            DataContract.PopularEntry.COLUMN_MOVIE_VOTES,
            DataContract.PopularEntry.COLUMN_MOVIE_ID,
            DataContract.PopularEntry.COLUMN_MOVIE_GENRES,
            DataContract.PopularEntry.COLUMN_MOVIE_THUMBNAIL,
            DataContract.PopularEntry.COLUMN_BACKDROP,
            DataContract.PopularEntry.COLUMN_DATE_GENERATED
    };
    public static final int COL_PROJ_TITLE = 0;
    public static final int COL_PROJ_PLOT = 1;
    public static final int COL_PROJ_RELEASE = 2;
    public static final int COL_PROJ_VOTES = 3;
    public static final int COL_PROJ_ID = 4;
    public static final int COL_PROJ_GENRES = 5;
    public static final int COL_PROJ_THUMBNAIL = 6;
    public static final int COL_PROJ_BACKDROP = 7;
    public static final int COL_PROJ_DATE_GENERATED = 8;
    public DetailFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void initialfavoriteDetector(){
        //checks ccvb
        Cursor c = getActivity().getContentResolver().query(
                DataContract.FavoriteEntry.CONTENT_URI,
                mProjectionMovieId,
                DataContract.FavoriteEntry.COLUMN_MOVIE_ID+"="+movieInDatabase.getString(COL_PROJ_ID),
                null,
                null);
        if(c.moveToFirst()){
            isCurrentlyFavorited(true);
        }else{
            isCurrentlyFavorited(false);
        }
    }

    public void callLoader(){
        //calls loader to add movie to favorites list
        getLoaderManager().initLoader(FAVORITES_LOADER,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent getMovieInfo = getActivity().getIntent();
        //get list passed from intent
        //String order = title,release,vote_average, thumbnail, plot, backdrop,id,genres
        String idholder = getMovieInfo.getStringExtra("idholder");
        String sortOrder = getMovieInfo.getStringExtra("sort");
        String[] args = {idholder};
        movieInDatabase=null;
        switch (sortOrder){
            case "top_rated":
                movieInDatabase = getActivity().getContentResolver().query(
                        DataContract.TopRatedEntry.CONTENT_URI, mProjection,
                        "movieID=?",args,null);
                break;
            case "popular":
                movieInDatabase = getActivity().getContentResolver().query(
                        DataContract.PopularEntry.CONTENT_URI, mProjection,
                        "movieID=?",args,null);
                break;
            case "upcoming":
                 movieInDatabase = getActivity().getContentResolver().query(
                        DataContract.UpcomingEntry.CONTENT_URI, mProjection,
                         "movieID=?",args,null);
                break;
            case "favorites":
                movieInDatabase = getActivity().getContentResolver().query(
                        DataContract.FavoriteEntry.CONTENT_URI, mProjection,
                        "movieID=?",args,null);
                break;
        }
        if(movieInDatabase.moveToFirst()) {
            String x = movieInDatabase.getString(COL_PROJ_GENRES);
        }
        viewInstantiator(rootView);
        fillRootView();
        return rootView;
    }

    private void fillRootView() {
        //1. Name, 2. Date, 3. Vote average, 4. Poster thumbnail, 5. Plot Analysis, 6. backdrop
        Picasso.with(getActivity())
                .load(URL_POSTER_HEADER + movieInDatabase.getString(COL_PROJ_BACKDROP))
                .placeholder(R.drawable.placeholder_red)
                .error(R.drawable.error_no_img_found)
                .fit()
                .into(img_backdrop);

        Picasso.with(getActivity())
                .load(URL_POSTER_HEADER + movieInDatabase.getString(COL_PROJ_THUMBNAIL))
                .placeholder(R.drawable.placeholder_red)
                .error(R.drawable.error_no_img_found)
                .fit()
                .into(img_poster);
        //plot overview
        tv_plot_overview.setText(movieInDatabase.getString(COL_PROJ_PLOT));
        //movie title
        tv_title.setText(movieInDatabase.getString(COL_PROJ_TITLE));
        //vote average
        tv_vote_average.setText(movieInDatabase.getString(COL_PROJ_VOTES));
        //release date
        tv_release.setText(dateFormatter(movieInDatabase.getString(COL_PROJ_RELEASE)));
        //genres
        tv_genres.setText(movieInDatabase.getString(COL_PROJ_GENRES));
    }

    //views from rootview
    private void viewInstantiator(View rootView) {
        img_backdrop = (ImageView)rootView.findViewById(R.id.img_backdrop);
        img_poster = (ImageView)rootView.findViewById(R.id.posterview);
        tv_plot_overview=(TextView)rootView.findViewById(R.id.tv_overview);
        tv_title = (TextView)rootView.findViewById(R.id.tv_movie_title);
        tv_vote_average = (TextView)rootView.findViewById(R.id.tv_vote_average);
        tv_release = (TextView)rootView.findViewById(R.id.tv_release_date);
        tv_genres = (TextView)rootView.findViewById(R.id.tv_genres);
        tv_cast =(TextView)rootView.findViewById(R.id.tv_cast);
        button_fav = (ImageButton)rootView.findViewById(R.id.favorites_select);
        button_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoader();
            }
        });
        tv_cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent castActivity = new Intent(getActivity(),CastCrewActivity.class);
                castActivity.putExtra("movie_id",movieInDatabase.getString(COL_PROJ_ID));
                castActivity.putExtra("movie_title",movieInDatabase.getString(COL_PROJ_TITLE));
                startActivity(castActivity);
            }
        });
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(movieInDatabase.getString(COL_PROJ_TITLE));
        img_videoThumbnail = (ImageView)rootView.findViewById(R.id.img_trailer_thumbnail);
        tv_videoThumbnailTitle=(TextView)rootView.findViewById(R.id.tv_trailer_thumbnail);
        //fills in star if already favorited
        initialfavoriteDetector();
    }

    //takes in numbered month from json date and converts to a worded version with proper format
    private String dateFormatter(String x){
        List<String> seperatedString = Arrays.asList(x.split("\\-"));
        switch (seperatedString.get(1)){
            case "01":seperatedString.set(1,getActivity().getResources().getString(R.string.month_jan));break;case "02":seperatedString.set(1,getActivity().getResources().getString(R.string.month_feb));break;
            case "03":seperatedString.set(1,getActivity().getResources().getString(R.string.month_march));break;case "04":seperatedString.set(1,getActivity().getResources().getString(R.string.month_april));break;
            case "05":seperatedString.set(1,getActivity().getResources().getString(R.string.month_may));break;case "06":seperatedString.set(1,getActivity().getResources().getString(R.string.month_june));break;
            case "07":seperatedString.set(1,getActivity().getResources().getString(R.string.month_july));break;case "08":seperatedString.set(1,getActivity().getResources().getString(R.string.month_august));break;
            case "09":seperatedString.set(1,getActivity().getResources().getString(R.string.month_sept));break;case "10":seperatedString.set(1,getActivity().getResources().getString(R.string.month_oct));break;
            case "11":seperatedString.set(1,getActivity().getResources().getString(R.string.month_nov));break;case "12":seperatedString.set(1,getActivity().getResources().getString(R.string.month_dec));break;
            default:break;
        }
        return seperatedString.get(1) +" "+ seperatedString.get(2) + ", "+seperatedString.get(0);
    }

    public void isCurrentlyFavorited(boolean x){
        if(x){
            button_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
        }else{
            button_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uriFavorites = DataContract.FavoriteEntry.CONTENT_URI;
        return new CursorLoader(
                getActivity(),
                uriFavorites,
                mProjectionMovieId,
                DataContract.FavoriteEntry.COLUMN_MOVIE_ID+"="+movieInDatabase.getString(COL_PROJ_ID),
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        //Coursér in french probably
        Cursor c = (Cursor)data;
        boolean theTruthJustifier = c.moveToFirst();
        if (theTruthJustifier){
            //makes into bordered fav icon
            isCurrentlyFavorited(false);
            //destroy method
            int FAV_CONTENT_URI = getContext().getContentResolver().delete(DataContract.FavoriteEntry.CONTENT_URI,
                    DataContract.FavoriteEntry.COLUMN_MOVIE_ID+"="+movieInDatabase.getString(COL_PROJ_ID),
                    null);
            //Destroy to stop an accidental loop!
            getLoaderManager().destroyLoader(FAVORITES_LOADER);
        } else if(!theTruthJustifier){
            //fills in fav icon
            isCurrentlyFavorited(true);
            //Add new value
            ContentValues values = new ContentValues();
            values.put(DataContract.FavoriteEntry.COLUMN_MOVIE_TITLE,movieInDatabase.getString(COL_PROJ_TITLE));
            values.put(DataContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE,movieInDatabase.getString(COL_PROJ_RELEASE));
            values.put(DataContract.FavoriteEntry.COLUMN_MOVIE_VOTES,movieInDatabase.getString(COL_PROJ_VOTES));
            values.put(DataContract.FavoriteEntry.COLUMN_MOVIE_THUMBNAIL,movieInDatabase.getString(COL_PROJ_THUMBNAIL));
            values.put(DataContract.FavoriteEntry.COLUMN_MOVIE_PLOT,movieInDatabase.getString(COL_PROJ_PLOT));
            values.put(DataContract.FavoriteEntry.COLUMN_BACKDROP,movieInDatabase.getString(COL_PROJ_BACKDROP));
            values.put(DataContract.FavoriteEntry.COLUMN_MOVIE_ID,movieInDatabase.getString(COL_PROJ_ID));
            values.put(DataContract.FavoriteEntry.COLUMN_MOVIE_GENRES,movieInDatabase.getString(COL_PROJ_GENRES));
            values.put(DataContract.FavoriteEntry.COLUMN_DATE_GENERATED,movieInDatabase.getString(COL_PROJ_DATE_GENERATED));
            //insert
            Uri FAV_CONTENT_URI = getContext().getContentResolver().insert(
                    DataContract.FavoriteEntry.CONTENT_URI,values
            );
            //Destroy to stop an accidental loop!
            getLoaderManager().destroyLoader(FAVORITES_LOADER);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {}
}
