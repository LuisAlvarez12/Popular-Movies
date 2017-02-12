package com.example.luisalvarez.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CastCrewView extends AppCompatActivity {

    /**
     * Created by luisalvarez on 1/20/17.
     * CastCrewView is parent to fragment CastGenerator.
     * Works in conjunction to provide cast and crew via a movie id
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_crew_view);
    }

}
