package com.example.luisalvarez.popularmovies.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.luisalvarez.popularmovies.BuildConfig;
import com.example.luisalvarez.popularmovies.DetailActivity;
import com.example.luisalvarez.popularmovies.data.DataContract;
import com.example.luisalvarez.popularmovies.R;
import com.example.luisalvarez.popularmovies.view.MovieCursorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by luisalvarez on 1/14/17.
 */

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    private GridView posterGrid;
    private RequestQueue requestQueue;
    final String URL_LANGUAGE = "language";
    final String URL_PAGE = "page";
    final String URL_KEY = "api_key";
    String sortOrder;

    String[] mProjectionTopRated = {
            DataContract.TopRatedEntry.COLUMN_MOVIE_ID,
            DataContract.TopRatedEntry.COLUMN_DATE_GENERATED,
            DataContract.TopRatedEntry.COLUMN_MOVIE_TITLE,
            DataContract.TopRatedEntry.COLUMN_MOVIE_THUMBNAIL,
            DataContract.TopRatedEntry._ID
    };
    String[] mProjectionPopular = {
            DataContract.PopularEntry.COLUMN_MOVIE_ID,
            DataContract.PopularEntry.COLUMN_DATE_GENERATED,
            DataContract.PopularEntry.COLUMN_MOVIE_TITLE,
            DataContract.PopularEntry.COLUMN_MOVIE_THUMBNAIL,
            DataContract.PopularEntry._ID
    };

    String[] mProjectionUpcoming = {
            DataContract.UpcomingEntry.COLUMN_MOVIE_ID,
            DataContract.UpcomingEntry.COLUMN_DATE_GENERATED,
            DataContract.UpcomingEntry.COLUMN_MOVIE_TITLE,
            DataContract.UpcomingEntry.COLUMN_MOVIE_THUMBNAIL,
            DataContract.UpcomingEntry._ID
    };



    public final int COLUMN_PROJ_MOVIE_ID = 0;
    public final int COLUMN_PROJ_DATE = 1;
    public final int COLUMN_PROJ_MOVIE_TITLE = 2;
    public final int COLUMN_PROJ_MOVIE_POSTER=3;

    private int MOVIE_LOADER = 0;

    private MovieCursorAdapter mMovieAdapter;

    public MovieFragment() {}

    private void callLoader(){
        getLoaderManager().initLoader(MOVIE_LOADER,null,this);
    }

    private void dateChecker(){
        Cursor cursorTopRated = getActivity()
                .getContentResolver()
                .query(DataContract.TopRatedEntry.CONTENT_URI,
                        mProjectionTopRated,
                        null,
                        null,
                        null);
        Cursor cursorPopular = getActivity()
                .getContentResolver()
                .query(DataContract.PopularEntry.CONTENT_URI,
                        mProjectionPopular,
                        null,
                        null,
                        null);
        Cursor cursorUpcoming = getActivity()
                .getContentResolver()
                .query(DataContract.UpcomingEntry.CONTENT_URI,
                        mProjectionUpcoming,
                        null,
                        null,
                        null);

        if (cursorTopRated.moveToFirst() && cursorPopular.moveToFirst() && cursorUpcoming.moveToFirst()){
           DateFormat df = new SimpleDateFormat("ddMMyyyy");
            String date = df.format(Calendar.getInstance().getTime());
            if (date.equals(cursorPopular.getString(COLUMN_PROJ_DATE)) && date.equals(cursorUpcoming.getString(COLUMN_PROJ_DATE))
                     && date.equals(cursorTopRated.getString(COLUMN_PROJ_DATE))){
                callLoader();
            }else{
                getActivity().getContentResolver().delete(DataContract.PopularEntry.CONTENT_URI,null,null);
                getActivity().getContentResolver().delete(DataContract.TopRatedEntry.CONTENT_URI,null,null);
                getActivity().getContentResolver().delete(DataContract.UpcomingEntry.CONTENT_URI,null,null);
            }
        }else{
            continueToFetchData();
        }
        //check db for date changes
    }

    private void continueToFetchData() {
        final String URL_BASE = "https://api.themoviedb.org/3/movie/"+sortOrder+"?";
        Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                .appendQueryParameter(URL_KEY, BuildConfig.Movie_DB_key)
                .appendQueryParameter(URL_LANGUAGE, "en-US")
                .appendQueryParameter(URL_PAGE, "1")
                .build();
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest arrayreq = getJsonObjectRequest(builtUri);
        requestQueue.add(arrayreq);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        posterGrid = (GridView)rootView.findViewById(R.id.grid_movie_layout);
        mMovieAdapter = new MovieCursorAdapter(getActivity(),null,0);
        posterGrid.setAdapter(mMovieAdapter);
        getBundle(getArguments());
        dateChecker();
        callLoader();
        posterGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                intent.putExtra("idholder",cursor.getString(COLUMN_PROJ_MOVIE_ID));
                intent.putExtra("sort",sortOrder);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void getBundle(Bundle b){
        if(b!=null){
            sortOrder=b.getString("sortOrder");
        }
    }

    private String genreKeytoName(String x){
        switch(x){
            case "28":x=getString(R.string.genre_action);break;case "12":x=getString(R.string.genre_adventure);break;case "16":x=getString(R.string.genre_animation);break;
            case "35":x=getString(R.string.genre_comedy);break;case "80":x=getString(R.string.genre_crime);break;case "99":x=getString(R.string.genre_documentary);break;
            case "18":x=getString(R.string.genre_drama);break;case "10751":x=getString(R.string.genre_family);break;case "14":x=getString(R.string.genre_fantasy);break;
            case "36":x=getString(R.string.genre_history);break;case "27":x=getString(R.string.genre_history);break;case "10402":x=getString(R.string.genre_music);break;
            case "9648":x=getString(R.string.genre_mystery);break;case "10749":x=getString(R.string.genre_romance);break;case "878":x=getString(R.string.genre_science_fiction);break;
            case "10770":x=getString(R.string.genre_tv_movie);break;case "53":x=getString(R.string.genre_thriller);break;case "10752":x=getString(R.string.genre_war);break;
            case "37":x=getString(R.string.genre_western);break;default:x="";
        }
        return x;
    }
    @Override
    public void onResume() {
        //kill loader to refresh when going back from DetailActivity
        getLoaderManager().restartLoader(MOVIE_LOADER,null,this);
        super.onResume();
    }
    @NonNull
    private JsonObjectRequest getJsonObjectRequest(Uri builtUri) {
        return new JsonObjectRequest(Request.Method.GET, builtUri.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String[] resultJsonArray;
                    final String JSON_GET_BACKDROP = getString(R.string.json_backdrop);
                    final String JSON_GET_TITLE = getString(R.string.json_title);
                    final String JSON_GET_PLOT = getString(R.string.json_overview);
                    final String JSON_GET_VOTES = getString(R.string.json_average);
                    final String JSON_GET_THUMBNAIL = getString(R.string.json_poster);
                    final String JSON_GET_RELEASE = getString(R.string.json_release);
                    final String JSON_GET_MOVIE_ID = getString(R.string.json_id);
                    final String JSON_GET_GENRES = getString(R.string.json_genre_id);
                    JSONArray jsonMovieInnerArray = response.getJSONArray("results");
                    resultJsonArray = new String[jsonMovieInnerArray.length()];

                    ContentValues[] resultJsonToCursor = new ContentValues[jsonMovieInnerArray.length()];
                    DateFormat df = new SimpleDateFormat("ddMMyyyy");
                    String date = df.format(Calendar.getInstance().getTime());
                    for (int i = 0; i < jsonMovieInnerArray.length(); i++) {
                        JSONObject innerJSONIterator = jsonMovieInnerArray.getJSONObject(i);
                        //following will loop through all possible genres to be converted into words instead of id keys
                        JSONArray genreArray = innerJSONIterator.getJSONArray(JSON_GET_GENRES);
                        String genresString ="";
                        for(int j=0;j<genreArray.length();j++){
                            if(j==0) {
                                genresString = genreKeytoName(genreArray.getString(j));
                            }else if(j>0){
                                genresString = genresString+", "+genreKeytoName(genreArray.getString(j));
                            }
                        }
//                        ContentValues cv = new ContentValues()
                        switch (sortOrder){
                            case "popular":
                                resultJsonToCursor[i] = new ContentValues();
                                resultJsonToCursor[i].put(DataContract.PopularEntry.COLUMN_MOVIE_TITLE,innerJSONIterator.getString(JSON_GET_TITLE));
                                resultJsonToCursor[i].put(DataContract.PopularEntry.COLUMN_MOVIE_RELEASE_DATE,innerJSONIterator.getString(JSON_GET_RELEASE)) ;
                                resultJsonToCursor[i].put(DataContract.PopularEntry.COLUMN_MOVIE_VOTES,innerJSONIterator.getString(JSON_GET_VOTES));
                                resultJsonToCursor[i].put(DataContract.PopularEntry.COLUMN_MOVIE_THUMBNAIL,innerJSONIterator.getString(JSON_GET_THUMBNAIL));
                                resultJsonToCursor[i].put(DataContract.PopularEntry.COLUMN_MOVIE_PLOT,innerJSONIterator.getString(JSON_GET_PLOT));
                                resultJsonToCursor[i].put(DataContract.PopularEntry.COLUMN_BACKDROP,innerJSONIterator.getString(JSON_GET_BACKDROP));
                                resultJsonToCursor[i].put(DataContract.PopularEntry.COLUMN_MOVIE_ID,innerJSONIterator.getString(JSON_GET_MOVIE_ID));
                                resultJsonToCursor[i].put(DataContract.PopularEntry.COLUMN_MOVIE_GENRES,genresString);
                                resultJsonToCursor[i].put(DataContract.PopularEntry.COLUMN_DATE_GENERATED,date);
                                break;
                            case "top_rated":
                                resultJsonToCursor[i] = new ContentValues();
                                resultJsonToCursor[i].put(DataContract.TopRatedEntry.COLUMN_MOVIE_TITLE,innerJSONIterator.getString(JSON_GET_TITLE));
                                resultJsonToCursor[i].put(DataContract.TopRatedEntry.COLUMN_MOVIE_RELEASE_DATE,innerJSONIterator.getString(JSON_GET_RELEASE)) ;
                                resultJsonToCursor[i].put(DataContract.TopRatedEntry.COLUMN_MOVIE_VOTES,innerJSONIterator.getString(JSON_GET_VOTES));
                                resultJsonToCursor[i].put(DataContract.TopRatedEntry.COLUMN_MOVIE_THUMBNAIL,innerJSONIterator.getString(JSON_GET_THUMBNAIL));
                                resultJsonToCursor[i].put(DataContract.TopRatedEntry.COLUMN_MOVIE_PLOT,innerJSONIterator.getString(JSON_GET_PLOT));
                                resultJsonToCursor[i].put(DataContract.TopRatedEntry.COLUMN_BACKDROP,innerJSONIterator.getString(JSON_GET_BACKDROP));
                                resultJsonToCursor[i].put(DataContract.TopRatedEntry.COLUMN_MOVIE_ID,innerJSONIterator.getString(JSON_GET_MOVIE_ID));
                                resultJsonToCursor[i].put(DataContract.TopRatedEntry.COLUMN_MOVIE_GENRES,genresString);
                                resultJsonToCursor[i].put(DataContract.TopRatedEntry.COLUMN_DATE_GENERATED,date);
                                break;
                            case "upcoming":
                                resultJsonToCursor[i] = new ContentValues();
                                resultJsonToCursor[i].put(DataContract.UpcomingEntry.COLUMN_MOVIE_TITLE,innerJSONIterator.getString(JSON_GET_TITLE));
                                resultJsonToCursor[i].put(DataContract.UpcomingEntry.COLUMN_MOVIE_RELEASE_DATE,innerJSONIterator.getString(JSON_GET_RELEASE)) ;
                                resultJsonToCursor[i].put(DataContract.UpcomingEntry.COLUMN_MOVIE_VOTES,innerJSONIterator.getString(JSON_GET_VOTES));
                                resultJsonToCursor[i].put(DataContract.UpcomingEntry.COLUMN_MOVIE_THUMBNAIL,innerJSONIterator.getString(JSON_GET_THUMBNAIL));
                                resultJsonToCursor[i].put(DataContract.UpcomingEntry.COLUMN_MOVIE_PLOT,innerJSONIterator.getString(JSON_GET_PLOT));
                                resultJsonToCursor[i].put(DataContract.UpcomingEntry.COLUMN_BACKDROP,innerJSONIterator.getString(JSON_GET_BACKDROP));
                                resultJsonToCursor[i].put(DataContract.UpcomingEntry.COLUMN_MOVIE_ID,innerJSONIterator.getString(JSON_GET_MOVIE_ID));
                                resultJsonToCursor[i].put(DataContract.UpcomingEntry.COLUMN_MOVIE_GENRES,genresString);
                                resultJsonToCursor[i].put(DataContract.UpcomingEntry.COLUMN_DATE_GENERATED,date);
                                break;

                        }
                        //String order = title,release,vote_average, thumbnail, plot, backdrop,id,genres
                    }
                    int bulk;
                    switch (sortOrder) {
                        case "popular":
                             bulk = getActivity()
                                    .getContentResolver()
                                    .bulkInsert(
                                            DataContract.PopularEntry.CONTENT_URI,
                                            resultJsonToCursor
                                    );
                            break;
                        case "top_rated":
                             bulk = getActivity()
                                    .getContentResolver()
                                    .bulkInsert(
                                            DataContract.TopRatedEntry.CONTENT_URI,
                                            resultJsonToCursor
                                    );
                            break;
                        case "upcoming":
                            bulk = getActivity()
                                    .getContentResolver()
                                    .bulkInsert(
                                            DataContract.UpcomingEntry.CONTENT_URI,
                                            resultJsonToCursor
                                    );
                            break;
                        default:
                            bulk = getActivity()
                                    .getContentResolver()
                                    .bulkInsert(
                                            DataContract.PopularEntry.CONTENT_URI,
                                            resultJsonToCursor
                                    );
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uri = null;
        switch (sortOrder){
            case "top_rated":
                uri = DataContract.TopRatedEntry.CONTENT_URI;
                return new CursorLoader(getActivity(),
                        uri,
                        mProjectionTopRated,
                        null,
                        null,
                        null);
            case "popular":
                uri = DataContract.PopularEntry.CONTENT_URI;
                return new CursorLoader(getActivity(),
                        uri,
                        mProjectionPopular,
                        null,
                        null,
                        null);
            case "upcoming":
                uri = DataContract.UpcomingEntry.CONTENT_URI;
                return new CursorLoader(getActivity(),
                        uri,
                        mProjectionUpcoming,
                        null,
                        null,
                        null);
            case "favorites":
                uri = DataContract.FavoriteEntry.CONTENT_URI;
                return new CursorLoader(getActivity(),
                        uri,
                        mProjectionUpcoming,
                        null,
                        null,
                        null);
            default:
                return new CursorLoader(getActivity(),
                        uri,
                        mProjectionUpcoming,
                        null,
                        null,
                        null);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
            mMovieAdapter.swapCursor((Cursor)data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
