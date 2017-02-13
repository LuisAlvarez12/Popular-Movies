package com.example.luisalvarez.popularmovies.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luisalvarez.popularmovies.CastCrewActivity;
import com.example.luisalvarez.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.luisalvarez.popularmovies.data.MovieContract;

/**
 * Created by luisalvarez on 1/16/17.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    private String movieIdForLoader ="";
    //image URL start
    public static final String[] mProjectionMovieId = {
            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,
            MovieContract.FavoriteEntry._ID
    };
    private int FAVORITES_LOADER = 0;
    private ImageView img_backdrop,img_poster,img_videoThumbnail;
    private TextView tv_plot_overview,tv_title,tv_vote_average,tv_release,tv_genres,tv_cast,tv_videoThumbnailTitle;
    private ImageButton button_fav;
    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";
    private ArrayList<String> movieList;
    public DetailFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void initialfavoriteDetector(){
        //checks ccvb
        Cursor c = getActivity().getContentResolver().query(
                MovieContract.FavoriteEntry.CONTENT_URI,
                mProjectionMovieId,
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID+"="+movieIdForLoader,
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
        movieList = getMovieInfo.getStringArrayListExtra("moviedata");
        movieIdForLoader=movieList.get(6);
        viewInstantiator(rootView);
        fillRootView(movieList);
        return rootView;
    }

    private void fillRootView(ArrayList<String> movieList) {
        //1. Name, 2. Date, 3. Vote average, 4. Poster thumbnail, 5. Plot Analysis, 6. backdrop
        Picasso.with(getActivity())
                .load(URL_POSTER_HEADER + movieList.get(5))
                .placeholder(R.drawable.placeholder_red)
                .error(R.drawable.error_no_img_found)
                .fit()
                .into(img_backdrop);

        Picasso.with(getActivity())
                .load(URL_POSTER_HEADER + movieList.get(3))
                .placeholder(R.drawable.placeholder_red)
                .error(R.drawable.error_no_img_found)
                .fit()
                .into(img_poster);
        //plot overview
        tv_plot_overview.setText(movieList.get(4));
        //movie title
        tv_title.setText(movieList.get(0));
        //vote average
        tv_vote_average.setText(movieList.get(2));
        //release date
        tv_release.setText(dateFormatter(movieList.get(1)));
        //genres
        tv_genres.setText(movieList.get(7));
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
                castActivity.putExtra("movie_id",movieList.get(6));
                castActivity.putExtra("movie_title",movieList.get(0));
                startActivity(castActivity);
            }
        });
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(movieList.get(0));
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
        Uri uriFavorites = MovieContract.FavoriteEntry.CONTENT_URI;
        return new CursorLoader(
                getActivity(),
                uriFavorites,
                mProjectionMovieId,
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID+"="+movieIdForLoader,
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
            int FAV_CONTENT_URI = getContext().getContentResolver().delete(MovieContract.FavoriteEntry.CONTENT_URI,
                    MovieContract.FavoriteEntry.COLUMN_MOVIE_ID+"="+movieIdForLoader,
                    null);
            //Destroy to stop an accidental loop!
            getLoaderManager().destroyLoader(FAVORITES_LOADER);
        } else if(!theTruthJustifier){
            //fills in fav icon
            isCurrentlyFavorited(true);
            //Add new value
            ContentValues values = new ContentValues();
            values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE,movieList.get(0));
            values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE,movieList.get(1));
            values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_VOTES,movieList.get(2));
            values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_THUMBNAIL,movieList.get(3));
            values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_PLOT,movieList.get(4));
            values.put(MovieContract.FavoriteEntry.COLUMN_BACKDROP,movieList.get(5));
            values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,movieList.get(6));
            values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_GENRES,movieList.get(7));
            //insert
            Uri FAV_CONTENT_URI = getContext().getContentResolver().insert(
                    MovieContract.FavoriteEntry.CONTENT_URI,values
            );
            //Destroy to stop an accidental loop!
            getLoaderManager().destroyLoader(FAVORITES_LOADER);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {}
}
