package com.example.luisalvarez.popularmovies;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.luisalvarez.popularmovies.data.MovieContract;

/**
 * Created by luisalvarez on 1/16/17.
 */

public class DetailFragment extends Fragment {

    //image URL start


    private ImageView img_backdrop,img_poster;
    private TextView tv_plot_overview,tv_title,tv_vote_average,tv_release,tv_genres,tv_cast;
    private Button button_fav,button_fav_go;
    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";
    public DetailFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent getMovieInfo = getActivity().getIntent();
        //get list passed from intent
        //String order = title,release,vote_average, thumbnail, plot, backdrop,id,genres
        final ArrayList<String> movieList = getMovieInfo.getStringArrayListExtra("moviedata");
        //set action bar title to movie title
//        getActivity().setTitle(movieList.get(0));

        viewInstantiator(rootView);
        fillRootView(movieList);
        button_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,movieList.get(0));
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,movieList.get(1));
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTES,movieList.get(2));
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_THUMBNAIL,movieList.get(3));
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_PLOT,movieList.get(4));
                values.put(MovieContract.MovieEntry.COLUMN_BACKDROP,movieList.get(5));
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movieList.get(6));
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_GENRES,movieList.get(7));
                Log.d("db","7");
                Uri FAV_CONTENT_URI = getContext().getContentResolver().insert(
                        MovieContract.MovieEntry.CONTENT_URI,values
                );
                Toast.makeText(getActivity(),"added!",Toast.LENGTH_SHORT).show();

            }
        });
        button_fav_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentF = new Intent(getActivity(),FavoritesLayout.class);
                startActivity(intentF);
            }
        });
        tv_cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent castActivity = new Intent(getActivity(),CastCrewView.class);
                castActivity.putExtra("movie_id",movieList.get(6));
                castActivity.putExtra("movie_title",movieList.get(0));
                startActivity(castActivity);

            }
        });
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(movieList.get(0));

        
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
        button_fav = (Button)rootView.findViewById(R.id.favorites_button);
        button_fav_go=(Button)rootView.findViewById(R.id.favorites_move);


    }


    //takes in numbered month from json date and converts to a worded version with proper format
    private String dateFormatter(String x){
        List<String> seperatedString = Arrays.asList(x.split("\\-"));
        switch (seperatedString.get(1)){
            case "01":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_jan));
                break;
            case "02":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_feb));
                break;
            case "03":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_march));
                break;
            case "04":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_april));
                break;
            case "05":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_may));
                break;
            case "06":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_june));
                break;
            case "07":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_july));
                break;
            case "08":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_august));
                break;
            case "09":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_sept));
                break;
            case "10":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_oct));
                break;
            case "11":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_nov));
                break;
            case "12":
                seperatedString.set(1,getActivity().getResources().getString(R.string.month_dec));
                break;
            default:
                break;
        }
        String completedString=seperatedString.get(1) +" "+ seperatedString.get(2) + ", "+seperatedString.get(0);
        return completedString;
    }

}
