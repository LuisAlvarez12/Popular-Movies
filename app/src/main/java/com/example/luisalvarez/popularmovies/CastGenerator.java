package com.example.luisalvarez.popularmovies;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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

import static android.R.id.list;
import static android.media.CamcorderProfile.get;

/**
 * Created by luisalvarez on 1/20/17.
 */

public class CastGenerator extends android.support.v4.app.Fragment {

    private ListView castList;
    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";
    public CastGenerator() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.crew_fragment, container, false);
        castList = (ListView)rootView.findViewById(R.id.listview_cast);
        Intent getIntent = getActivity().getIntent();
        String movieID = getIntent.getStringExtra("movie_id");
        getActivity().setTitle(getIntent.getStringExtra("movie_title"));
        CastFetcher c = new CastFetcher();
        c.execute(movieID);
        return rootView;
    }


    public class CastFetcher extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPostExecute(String[] s) {
            Log.d("layout",s[0]);
            CastListAdapter listAdapter = new CastListAdapter(getActivity(),R.layout.listview_item_layout,s);
            castList.setAdapter(listAdapter);

        }

        @Override
        protected String[] doInBackground(String... params) {

            //instantiate a http connection and reader for future use in the try/catch
            HttpURLConnection httpQueryConnection = null;
            BufferedReader reader = null;
            //This string will hold the designated output of the buffer
            String moviesJsonOutput;

            try {
                //query url builder
                //popular?
                //https://api.themoviedb.org/3/movie/121856/credits?api_key=e10725c4a50b8f4e6c04c49461910360

                final String URL_BASE = "https://api.themoviedb.org/3/movie/" + params[0] + "/credits?";
                final String URL_KEY = "api_key";

                //uri build
                Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                        .appendQueryParameter(URL_KEY, BuildConfig.Movie_DB_key)
                        .build();
                //URL getting the output from the built URI
                URL query = new URL(builtUri.toString());
                Log.d("cast", builtUri.toString());

                //Open the connection
                httpQueryConnection = (HttpURLConnection) query.openConnection();
                httpQueryConnection.setRequestMethod("GET");
                httpQueryConnection.connect();

                //Inputstream gets the flowing data from the http connection and stores it to be buffered out
                InputStream inStream = httpQueryConnection.getInputStream();
                StringBuffer jsonIncomingBuffer = new StringBuffer();
                //return null if the inputstream sees nothing
                if (inStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inStream));
                //String to recieve each line from the bufferedreader as a holder
                String line;
                //Make line equal each incoming line of the buffer, and until there are no more, continue appending
                while ((line = reader.readLine()) != null) {
                    jsonIncomingBuffer.append(line + "\n");
                }
                //If buffer output nothing, nullify
                if (jsonIncomingBuffer.length() == 0) {
                    return null;
                }
                //toString on the output StringBuffer
                //This is the line holding the ouput of the JSON
                moviesJsonOutput = jsonIncomingBuffer.toString();
            } catch (IOException e) {
                return null;
            } finally {
                if (httpQueryConnection != null) {
                    httpQueryConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //array output
            return stringToJSONArray(moviesJsonOutput);
        }

        private String[] stringToJSONArray(String jsonString) {
            String[] resultJsonArray = null;

            final String JSON_GET_CAST_NAME = "name";
            final String JSON_GET_CAST_CHAR = "character";
            final String JSON_GET_CAST_JOB = "job";

            final String JSON_GET_CAST_PROFILE_PATH = "profile_path";

            try {
                JSONObject jsonCastInput = new JSONObject(jsonString);
                JSONArray jsonCastJSONArray = jsonCastInput.getJSONArray("cast");
                JSONArray jsonCrewJSONArray = jsonCastInput.getJSONArray("crew");
                resultJsonArray = new String[jsonCastJSONArray.length()+jsonCrewJSONArray.length()];
                for (int i = 0; i < jsonCastJSONArray.length(); i++) {
                    JSONObject innerJSONIterator = jsonCastJSONArray.getJSONObject(i);
                    //name,character,profile
                    String jsonToStringWithCommas =
                            innerJSONIterator.getString(JSON_GET_CAST_NAME) + "|" +
                                    innerJSONIterator.getString(JSON_GET_CAST_CHAR) + "|" +
                                    innerJSONIterator.getString(JSON_GET_CAST_PROFILE_PATH);
                    resultJsonArray[i] = jsonToStringWithCommas;
                }
                for (int i = 0; i < jsonCrewJSONArray.length(); i++) {
                    JSONObject innerJSONIterator = jsonCrewJSONArray.getJSONObject(i);
                    //name,character,profile
                    String jsonToStringWithCommas =
                            innerJSONIterator.getString(JSON_GET_CAST_NAME) + "|" +
                                    innerJSONIterator.getString(JSON_GET_CAST_JOB) + "|" +
                                    innerJSONIterator.getString(JSON_GET_CAST_PROFILE_PATH);
                    resultJsonArray[i+jsonCastJSONArray.length()] = jsonToStringWithCommas;
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            return resultJsonArray;
        }
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
            TextView castActorCharacter = (TextView)rootView.findViewById(R.id.tv_actor_character);
            TextView castActorName = (TextView)rootView.findViewById(R.id.tv_actor_name);
            ImageView castActorProfile = (ImageView)rootView.findViewById(R.id.img_actor_profile);

            List<String> jsonSeperate = Arrays.asList(castInfo[position].split("\\|"));
            castActorName.setText(jsonSeperate.get(0));
            castActorCharacter.setText(jsonSeperate.get(1));
            Picasso.with(c) //
                    .load(URL_POSTER_HEADER + jsonSeperate.get(2))
                    .error(R.drawable.error_actor)
                    .into(castActorProfile);
            return rootView;
        }
    }
}
