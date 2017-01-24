package com.example.luisalvarez.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by luisalvarez on 1/20/17.
 */

public class CastGenerator extends android.support.v4.app.Fragment {

    //Cast/crew listview
    private ListView castList;
    private RequestQueue requestQueue;

    //image url header
    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";
    public CastGenerator() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crew, container, false);
        castList = (ListView)rootView.findViewById(R.id.listview_cast);
        Intent getIntent = getActivity().getIntent();
        //retrieve the id to generate the credits list
        String movieID = getIntent.getStringExtra("movie_id");
        getActivity().setTitle(getIntent.getStringExtra("movie_title"));

        final String URL_BASE = "https://api.themoviedb.org/3/movie/" + movieID + "/credits?";
        final String URL_KEY = "api_key";
        //Built URI to append queries correctly
        Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                .appendQueryParameter(URL_KEY, BuildConfig.Movie_DB_key)
                .build();
        //Instantiate Volley queue
        requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest arrayreq = getJsonObjectRequest(builtUri);
        requestQueue.add(arrayreq);
        return rootView;
    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest(Uri builtUri) {
        return new JsonObjectRequest(Request.Method.GET, builtUri.toString(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                        //array keys
                        String[] resultJsonArray;
                        final String JSON_GET_CAST_NAME = "name";
                        final String JSON_GET_CAST_CHAR = "character";
                        final String JSON_GET_CAST_JOB = "job";
                        final String JSON_GET_CAST_PROFILE_PATH = "profile_path";

                        try {
                            JSONObject jsonCastInput = response;
                            JSONArray jsonCastJSONArray = jsonCastInput.getJSONArray("cast");
                            JSONArray jsonCrewJSONArray = jsonCastInput.getJSONArray("crew");
                            //array with cast + crew length
                            resultJsonArray = new String[jsonCastJSONArray.length()+jsonCrewJSONArray.length()];
                            for (int i = 0; i < jsonCastJSONArray.length(); i++) {
                                //cast info
                                JSONObject innerJSONIterator = jsonCastJSONArray.getJSONObject(i);
                                //name,character,profile
                                String jsonToStringWithCommas =
                                        innerJSONIterator.getString(JSON_GET_CAST_NAME) + "|" +
                                                innerJSONIterator.getString(JSON_GET_CAST_CHAR) + "|" +
                                                innerJSONIterator.getString(JSON_GET_CAST_PROFILE_PATH);
                                resultJsonArray[i] = jsonToStringWithCommas;
                            }
                            for (int i = 0; i < jsonCrewJSONArray.length(); i++) {
                                //crew info
                                JSONObject innerJSONIterator = jsonCrewJSONArray.getJSONObject(i);
                                //name,job,profile
                                String jsonToStringWithCommas =
                                        innerJSONIterator.getString(JSON_GET_CAST_NAME) + "|" +
                                                innerJSONIterator.getString(JSON_GET_CAST_JOB) + "|" +
                                                innerJSONIterator.getString(JSON_GET_CAST_PROFILE_PATH);
                                resultJsonArray[i+jsonCastJSONArray.length()] = jsonToStringWithCommas;
                            }
                            CastListAdapter listAdapter = new CastListAdapter(getActivity(), R.layout.listview_item_layout,resultJsonArray);
                            castList.setAdapter(listAdapter);
                        }
                        catch(JSONException e) {

                        }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
    }

    public class CastListAdapter extends ArrayAdapter{

        Activity c;
        private String[] castInfo;

        public CastListAdapter(Activity context, int resource,String[] s) {
            super(context, R.layout.listview_item_layout,s);
            this.c=context;
            this.castInfo=s;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = c.getLayoutInflater();
            View rootView =inflater.inflate(R.layout.listview_item_layout,null,true);
            //instantiate views
            TextView castActorCharacter = (TextView)rootView.findViewById(R.id.tv_actor_character);
            TextView castActorName = (TextView)rootView.findViewById(R.id.tv_actor_name);
            ImageView castActorProfile = (ImageView)rootView.findViewById(R.id.img_actor_profile);

            //seperate json string into list
            List<String> jsonSeperate = Arrays.asList(castInfo[position].split("\\|"));
            castActorName.setText(jsonSeperate.get(0));
            castActorCharacter.setText(jsonSeperate.get(1));
            Picasso.with(c) //
                    .load(URL_POSTER_HEADER + jsonSeperate.get(2))
                    .error(R.drawable.error_no_img_found)
                    .into(castActorProfile);
            return rootView;
        }
    }
}
