package com.example.luisalvarez.popularmovies.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.luisalvarez.popularmovies.BuildConfig;
import com.example.luisalvarez.popularmovies.view.PosterImageAdapter;
import com.example.luisalvarez.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luisalvarez on 1/14/17.
 */

public class MovieFragment extends Fragment {

    private GridView posterGrid;
    private RequestQueue requestQueue;
    final String URL_LANGUAGE = "language";
    final String URL_PAGE = "page";
    final String URL_KEY = "api_key";
    String sortOrder;

    public MovieFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        posterGrid = (GridView)rootView.findViewById(R.id.grid_movie_layout);

        getBundle(getArguments());
        final String URL_BASE = "https://api.themoviedb.org/3/movie/"+sortOrder+"?";
        Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                .appendQueryParameter(URL_KEY, BuildConfig.Movie_DB_key)
                .appendQueryParameter(URL_LANGUAGE, "en-US")
                .appendQueryParameter(URL_PAGE, "1")
                .build();
        requestQueue = Volley.newRequestQueue(getActivity());
        Log.d("Volley", builtUri.toString());
        JsonObjectRequest arrayreq = getJsonObjectRequest(builtUri);
        requestQueue.add(arrayreq);
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
                            //String order = title,release,vote_average, thumbnail, plot, backdrop,id,genres
                            String jsonToStringWithCommas =
                                    innerJSONIterator.getString(JSON_GET_TITLE) + "|" +
                                            innerJSONIterator.getString(JSON_GET_RELEASE) + "|" +
                                            innerJSONIterator.getString(JSON_GET_VOTES) + "|" +
                                            innerJSONIterator.getString(JSON_GET_THUMBNAIL) + "|" +
                                            (innerJSONIterator.getString(JSON_GET_PLOT)) + "|" +
                                            innerJSONIterator.getString(JSON_GET_BACKDROP)+ "|" +
                                            innerJSONIterator.getString(JSON_GET_MOVIE_ID)+ "|" +
                                            genresString;
                            resultJsonArray[i] = jsonToStringWithCommas;
                        }
                        posterGrid.setAdapter(new PosterImageAdapter(getActivity(),resultJsonArray));
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

}
