package com.example.luisalvarez.popularmovies.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.luisalvarez.popularmovies.BuildConfig;
import com.example.luisalvarez.popularmovies.CastCrewActivity;
import com.example.luisalvarez.popularmovies.R;
import com.example.luisalvarez.popularmovies.data.DataContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static android.R.attr.thumbnail;
import static com.example.luisalvarez.popularmovies.R.string.reviews;

/**
 * Created by luisalvarez on 1/16/17.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks{
    //image URL start
    private RequestQueue requestQueue;

    public static final String[] mProjectionMovieId = {
            DataContract.FavoriteEntry.COLUMN_MOVIE_ID,
            DataContract.FavoriteEntry._ID
    };
    private int FAVORITES_LOADER = 0;
    private ImageView img_backdrop,img_poster,img_videoThumbnail;
    private TextView tv_plot_overview,tv_title,tv_vote_average,tv_release,tv_genres,tv_cast;
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

    private LinearLayout horizontalLayoutReviews;
    private LinearLayout horizontalLayoutVideos;


    public DetailFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent getMovieInfo = getActivity().getIntent();
        String idholder = getMovieInfo.getStringExtra("idholder");
        String sortOrder = getMovieInfo.getStringExtra("sort");
        String[] args = {idholder};
        determineOriginTable(sortOrder, args);
        final String URL_KEY = getActivity().getResources().getString(R.string.url_key);

        //https://api.themoviedb.org/3/movie/244786/reviews?api_key=e10725c4a50b8f4e6c04c49461910360&language=en-US&page=1
        String reviewParam = "/reviews?";
        String videoParam = "/videos?";
        final String URL_BASE = "https://api.themoviedb.org/3/movie/"+movieInDatabase.getString(COL_PROJ_ID);
        Uri reviewsUri = Uri.parse(URL_BASE +reviewParam).buildUpon()
                .appendQueryParameter(URL_KEY, BuildConfig.Movie_DB_key)
                .build();
        Uri videosUri = Uri.parse(URL_BASE +videoParam).buildUpon()
                .appendQueryParameter(URL_KEY, BuildConfig.Movie_DB_key)
                .build();
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest videoRequest = getVideosFromJsonToLayout(videosUri);
        JsonObjectRequest reviewRequest = getReviewsFromJsonToLayout(reviewsUri);
        requestQueue.add(videoRequest);
        requestQueue.add(reviewRequest);
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
        horizontalLayoutReviews = (LinearLayout)rootView.findViewById(R.id.linear_layout_horizontal_scroll);
        horizontalLayoutVideos=(LinearLayout)rootView.findViewById(R.id.linear_layout_horizontal_scroll_videos);
        //fills in star if already favorited
        initialfavoriteDetector();
    }

    private void determineOriginTable(String sortOrder, String[] args) {
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
        if(movieInDatabase.moveToFirst()) {}
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

   //Volley Json requests
   @NonNull
   private JsonObjectRequest getVideosFromJsonToLayout(Uri builtUri) {
       return new JsonObjectRequest(Request.Method.GET, builtUri.toString(), new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               try {
                   Log.d("vid","1");
                   final String JSON_GET_VIDEO_YT_KEY = "key";
                   final String JSON_GET_VIDEO_YT_NAME = "name";

                   JSONArray jsonMovieInnerArray = response.getJSONArray("results");

                   ContentHolderVideos[] stringHolders = new ContentHolderVideos[jsonMovieInnerArray.length()];

                   for (int i = 0; i < jsonMovieInnerArray.length(); i++) {


                       JSONObject innerJSONIterator = jsonMovieInnerArray.getJSONObject(i);

                       stringHolders[i] = new ContentHolderVideos(innerJSONIterator.getString(JSON_GET_VIDEO_YT_NAME),
                               innerJSONIterator.getString(JSON_GET_VIDEO_YT_KEY));

                   }
                   fillHorizontalScrollViewVideos(stringHolders);

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


    //Fill the videos and reviews section
    private void fillHorizontalScrollViewReviews(final ContentHolderReviews[] contentHolders){
        int dimension = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        if(contentHolders.length==0 || contentHolders==null){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dimension,dimension,dimension,dimension);
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(params);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.error_no_reviews_found));
            horizontalLayoutReviews.addView(imageView);


        }else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT);


            params.setMargins(dimension, dimension, dimension, dimension);

            for (int i = 0; i < contentHolders.length; i++) {
                //create Linear layout
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setLayoutParams(params);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                TextView title = new TextView(getActivity());
                title.setText(getActivity().getResources().getString(R.string.fillhorizontal_by_author) + " " + contentHolders[i].author);
                title.setGravity(Gravity.CENTER);
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                title.setLayoutParams(params);
                TextView content = new TextView(getActivity());
                content.getBaseline();
                content.setMaxLines(7);
                content.setText(contentHolders[i].id);
                content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                content.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                content.setGravity(Gravity.CENTER);
                TextView nextPage = new TextView(getActivity());
                nextPage.setGravity(Gravity.CENTER);
                nextPage.setLayoutParams(params);
                nextPage.setText(getActivity().getResources().getString(R.string.fillhorizontal_full_review));
                nextPage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                nextPage.setTextColor(Color.BLACK);

                final int finalI = i;

                //https://www.youtube.com/watch?v=
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://www.themoviedb.org/review/" + contentHolders[finalI].content;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                });
                linearLayout.addView(title);
                linearLayout.addView(content);
                linearLayout.addView(nextPage);
                horizontalLayoutReviews.addView(linearLayout);

            }
        }
    }

    private void fillHorizontalScrollViewVideos(final ContentHolderVideos[] contentHolders){
        int dimension = (int)getResources().getDimension(R.dimen.activity_horizontal_margin);

        if(contentHolders.length==0 || contentHolders==null){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dimension,dimension,dimension,dimension);
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(params);

            imageView.setImageDrawable(getResources().getDrawable(R.drawable.error_no_video_found));
            horizontalLayoutVideos.addView(imageView);


        }else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dimension, dimension, dimension, dimension);

            final String ytLinkHeader = "https://www.youtube.com/watch?v=";
            for (int i = 0; i < contentHolders.length; i++) {
                String ytHeader = "http://img.youtube.com/vi/" + contentHolders[i].link + "/0.jpg";
                //create Linear layout
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setLayoutParams(params);
                linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                FrameLayout frameLayout = new FrameLayout(getActivity());
                frameLayout.setLayoutParams(params);


                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 500));

                ImageView icon = new ImageView(getActivity());
                icon.setLayoutParams(new LinearLayout.LayoutParams(180, 180));
                icon.setImageDrawable(getResources().getDrawable(R.drawable.yt_icon));
                frameLayout.addView(imageView);
                frameLayout.addView(icon);

                TextView nextPage = new TextView(getActivity());
                nextPage.setGravity(Gravity.CENTER);
                nextPage.setLayoutParams(params);
                nextPage.setText(contentHolders[i].name);
                nextPage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                nextPage.setTextColor(Color.BLACK);

                final int finalI = i;

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = ytLinkHeader + contentHolders[finalI].link;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                });

                linearLayout.addView(frameLayout);
                linearLayout.addView(nextPage);
                horizontalLayoutVideos.addView(linearLayout);
                Picasso.with(getContext()) //
                        .load(ytHeader) //
                        .placeholder(getActivity().getResources().getDrawable(R.drawable.placeholder_red)) //
                        .error(getActivity().getResources().getDrawable(R.drawable.error_no_img_found)) //
                        .fit() //
                        .into(imageView);

            }
        }
    }


    @NonNull
    private JsonObjectRequest getReviewsFromJsonToLayout(Uri builtUri) {
        return new JsonObjectRequest(Request.Method.GET, builtUri.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String JSON_GET_REVIEW_CONTENT = getActivity().getResources().getString(R.string.json_video_getter_content);
                    final String JSON_GET_REVIEW_ID = getActivity().getResources().getString(R.string.json_video_getter_id);
                    final String JSON_GET_REVIEW_AUTHOR =getActivity().getResources().getString(R.string.json_video_getter_author);
                    JSONArray jsonMovieInnerArray = response.getJSONArray("results");
                    ContentHolderReviews[] stringHolders = new ContentHolderReviews[jsonMovieInnerArray.length()];
                    for (int i = 0; i < jsonMovieInnerArray.length(); i++) {
                        JSONObject innerJSONIterator = jsonMovieInnerArray.getJSONObject(i);
                        stringHolders[i] = new ContentHolderReviews(innerJSONIterator.getString(JSON_GET_REVIEW_AUTHOR),
                                innerJSONIterator.getString(JSON_GET_REVIEW_ID),
                                innerJSONIterator.getString(JSON_GET_REVIEW_CONTENT));
                    }
                    fillHorizontalScrollViewReviews(stringHolders);

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

    class ContentHolderReviews {
        String author,content,id;
        private ContentHolderReviews(String a, String c, String i){
            author=a;
            content=c;
            id=i;
        }
    }
    class ContentHolderVideos{
        String name,link;

        private ContentHolderVideos(String n, String l){
            name=n;
            link=l;
        }
    }


    //Loader setup and callback
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

    public void isCurrentlyFavorited(boolean x){
        if(x){
            button_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
        }else{
            button_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
        }
    }

    public void callLoader(){
        //calls loader to add movie to favorites list
        getLoaderManager().initLoader(FAVORITES_LOADER,null,this);
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

}

