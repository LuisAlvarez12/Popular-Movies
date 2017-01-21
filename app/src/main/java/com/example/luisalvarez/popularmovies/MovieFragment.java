package com.example.luisalvarez.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by luisalvarez on 1/14/17.
 */

public class MovieFragment extends Fragment {

    private String[] posterUrlFooters;
    private int[] pageIncrementor = {1,1,1};
    private GridView posterGrid;
    String sortOrder;

    public MovieFragment() {

    }

    public MovieFragment(String s) {
        sortOrder=s;

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
            posterGrid.setAdapter(new PosterImageAdapter(getActivity(),jsonArrayAsStringResult));
        }

        //returns the current page for fetching more pages of information
        private String returnCurrentPage(){
            String fin="";
            switch (sortOrder){
                case "popular":
                     fin=""+pageIncrementor[0];
                    break;
                case "top_rated":
                     fin=""+pageIncrementor[1];
                break;
                case "upcoming":
                     fin=""+pageIncrementor[2];
                break;
            }
            return fin;
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
                //popular?
                final String URL_BASE = "https://api.themoviedb.org/3/movie/"+sortOrder+"?";
                final String URL_LANGUAGE = "language";
                final String URL_PAGE = "page";
                final String URL_KEY = "api_key";

                //uri build
                Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                        .appendQueryParameter(URL_KEY, BuildConfig.Movie_DB_key)
                        .appendQueryParameter(URL_LANGUAGE, "en-US")
                        .appendQueryParameter(URL_PAGE, ""+returnCurrentPage())
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

        //recieves genre id, converts to given value, and returns worded version
        private String genreKeytoName(String x){

            switch(x){
                case "28":x=getString(R.string.genre_action);break;
                case "12":x=getString(R.string.genre_adventure);break;
                case "16":x=getString(R.string.genre_animation);break;
                case "35":x=getString(R.string.genre_comedy);break;
                case "80":x=getString(R.string.genre_crime);break;
                case "99":x=getString(R.string.genre_documentary);break;
                case "18":x=getString(R.string.genre_drama);break;
                case "10751":x=getString(R.string.genre_family);break;
                case "14":x=getString(R.string.genre_fantasy);break;
                case "36":x=getString(R.string.genre_history);break;
                case "27":x=getString(R.string.genre_history);break;
                case "10402":x=getString(R.string.genre_music);break;
                case "9648":x=getString(R.string.genre_mystery);break;
                case "10749":x=getString(R.string.genre_romance);break;
                case "878":x=getString(R.string.genre_science_fiction);break;
                case "10770":x=getString(R.string.genre_tv_movie);break;
                case "53":x=getString(R.string.genre_thriller);break;
                case "10752":x=getString(R.string.genre_war);break;
                case "37":x=getString(R.string.genre_western);break;
                default:x="";
            }
            return x;
        }

        private String[] stringToJSONArray(String jsonString) {
            String[] resultJsonArray = null;
            final String JSON_GET_BACKDROP = getString(R.string.json_backdrop);
            final String JSON_GET_TITLE = getString(R.string.json_title);
            final String JSON_GET_PLOT = getString(R.string.json_overview);
            final String JSON_GET_VOTES = getString(R.string.json_average);
            final String JSON_GET_THUMBNAIL = getString(R.string.json_poster);
            final String JSON_GET_RELEASE = getString(R.string.json_release);
            final String JSON_GET_MOVIE_ID = getString(R.string.json_id);
            final String JSON_GET_GENRES = getString(R.string.json_genre_id);

            try {
                JSONObject jsonMovieInput = new JSONObject(jsonString);
                JSONArray jsonMovieInnerArray = jsonMovieInput.getJSONArray("results");
                resultJsonArray = new String[jsonMovieInnerArray.length()];
                for (int i = 0; i < jsonMovieInnerArray.length(); i++) {
                    JSONObject innerJSONIterator = jsonMovieInnerArray.getJSONObject(i);
                    String title = innerJSONIterator.getString(JSON_GET_TITLE);
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
