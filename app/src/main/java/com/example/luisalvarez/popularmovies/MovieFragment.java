package com.example.luisalvarez.popularmovies;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luisalvarez on 1/14/17.
 */

public class MovieFragment extends Fragment {

    private String[] posterUrlFooters;
    private GridView posterGrid;

    public MovieFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        posterGrid = (GridView)rootView.findViewById(R.id.grid_movie_layout);
        MovieFetcher m = new MovieFetcher();
        m.execute();
        return rootView;
    }

    public class MovieFetcher extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPostExecute(String[] jsonArrayAsStringResult) {
//            ArrayList<String> arrayList = new ArrayList<String>();
//
//           for(int i=0;i<posterUrlFooters.length;i++) {
//               List<String> list = Arrays.asList(jsonArrayAsStringResult[i].split("\\|"));
//               arrayList.add(list.get(3));
//           }
            posterGrid.setAdapter(new PosterImageAdapter(getActivity(),jsonArrayAsStringResult));

        }

        @Override
        protected String[] doInBackground(String... params) {
            //Main networking code
            //https://api.themoviedb.org/3/movie/popular?api_key=e10725c4a50b8f4e6c04c49461910360&language=en-US&page=1

            //instantiate a http connection and reader for future use in the try/catch
            HttpURLConnection httpQueryConnection = null;
            BufferedReader reader = null;
            //This string will hold the designated output of the buffer
            String moviesJsonOutput;

            try {
                //query url builder
                final String URL_BASE = "https://api.themoviedb.org/3/movie/popular?";
                final String URL_LANGUAGE = "language";
                final String URL_PAGE = "page";
                final String URL_KEY = "api_key";

                //uri build
                Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                        .appendQueryParameter(URL_KEY, "e10725c4a50b8f4e6c04c49461910360")
                        .appendQueryParameter(URL_LANGUAGE, "en-US")
                        .appendQueryParameter(URL_PAGE, "1")
                        .build();
                //URL getting the output from the built URI
                URL query = new URL(builtUri.toString());

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
            final String JSON_GET_BACKDROP = "backdrop_path";
            final String JSON_GET_TITLE = "original_title";
            final String JSON_GET_PLOT = "overview";
            final String JSON_GET_VOTES = "vote_average";
            final String JSON_GET_THUMBNAIL = "poster_path";
            final String JSON_GET_RELEASE = "release_date";
            try {
                JSONObject jsonMovieInput = new JSONObject(jsonString);
                JSONArray jsonMovieInnerArray = jsonMovieInput.getJSONArray("results");
                resultJsonArray = new String[jsonMovieInnerArray.length()];
                for (int i = 0; i < jsonMovieInnerArray.length(); i++) {
                    //   Log.d("output",i+ "    " + jsonMovieInnerArray.get(i).toString());
                    JSONObject innerJSONIterator = jsonMovieInnerArray.getJSONObject(i);
                    String title = innerJSONIterator.getString(JSON_GET_TITLE);
                    //String order = title,release,vote_average, thumbnail, plot, backdrop
                    String jsonToStringWithCommas =
                            innerJSONIterator.getString(JSON_GET_TITLE) + "|" +
                                    innerJSONIterator.getString(JSON_GET_RELEASE) + "|" +
                                    innerJSONIterator.getString(JSON_GET_VOTES) + "|" +
                                    innerJSONIterator.getString(JSON_GET_THUMBNAIL) + "|" +
                                    innerJSONIterator.getString(JSON_GET_PLOT) + "|" +
                                    innerJSONIterator.getString(JSON_GET_BACKDROP);
//                    Log.d("output",i+ "    " +jsonToStringWithCommas);
                    resultJsonArray[i] = jsonToStringWithCommas;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (resultJsonArray != null) {
                return resultJsonArray;
            } else {
                return null;
            }
        }
    }
}
