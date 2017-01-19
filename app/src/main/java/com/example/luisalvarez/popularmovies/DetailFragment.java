package com.example.luisalvarez.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.id.list;

/**
 * Created by luisalvarez on 1/16/17.
 */

public class DetailFragment extends Fragment {

    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";
    private ImageView img_backdrop,img_poster;
    private TextView tv_plot_overview,tv_title,tv_vote_average,tv_release,tv_genres;

    public DetailFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent getMovieInfo = getActivity().getIntent();
        //1. Name, 2. Date, 3. Vote average, 4. Poster thumbnail, 5. Plot Analysis, 6. backdrop
        ArrayList<String> movieList = getMovieInfo.getStringArrayListExtra("moviedata");
        getActivity().setTitle(movieList.get(0));
        img_backdrop = (ImageView)rootView.findViewById(R.id.img_backdrop);
        img_poster = (ImageView)rootView.findViewById(R.id.posterview);
        tv_plot_overview=(TextView)rootView.findViewById(R.id.tv_overview);
        tv_title = (TextView)rootView.findViewById(R.id.tv_movie_title);
        tv_vote_average = (TextView)rootView.findViewById(R.id.tv_vote_average);
        tv_release = (TextView)rootView.findViewById(R.id.tv_release_date);
        tv_genres = (TextView)rootView.findViewById(R.id.tv_genres);

        Picasso.with(getActivity()) //
                .load(URL_POSTER_HEADER + movieList.get(5)) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit()
                .into(img_backdrop);

        Picasso.with(getActivity()) //
                .load(URL_POSTER_HEADER + movieList.get(3)) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit()
                .into(img_poster);

        tv_plot_overview.setText(movieList.get(4));
        tv_title.setText(movieList.get(0));
        tv_vote_average.setText(movieList.get(2));
        tv_release.setText(dateFormatter(movieList.get(1)));
        tv_genres.setText(movieList.get(7));

        return rootView;
    }

    private String dateFormatter(String x){
        List<String> seperatedString = Arrays.asList(x.split("\\-"));
        switch (seperatedString.get(1)){
            case "01":
                seperatedString.set(1,"January");
                break;
            case "02":
                seperatedString.set(1,"February");
                break;
            case "03":
                seperatedString.set(1,"March");
                break;
            case "04":
                seperatedString.set(1,"April");
                break;
            case "05":
                seperatedString.set(1,"May");
                break;
            case "06":
                seperatedString.set(1,"June");
                break;
            case "07":
                seperatedString.set(1,"July");
                break;
            case "08":
                seperatedString.set(1,"August");
                break;
            case "09":
                seperatedString.set(1,"September");
                break;
            case "10":
                seperatedString.set(1,"October");
                break;
            case "11":
                seperatedString.set(1,"November");
                break;
            case "12":
                seperatedString.set(1,"December");
                break;
            default:
                break;
        }
        String completedString=seperatedString.get(1) +" "+ seperatedString.get(2) + ", "+seperatedString.get(0);
        return completedString;
    }


        public class ImageDetailLoader extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            Picasso.with(getActivity()) //
                    .load(URL_POSTER_HEADER + params) //
                    .placeholder(R.drawable.placeholder) //
                    .error(R.drawable.error) //
                    .into(img_backdrop);
            return "a";
        }
    }
}
