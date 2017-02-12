package com.example.luisalvarez.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class DetailActivity extends AppCompatActivity {

    /**
     * Created by luisalvarez on 1/20/17.
     * DetailActivity is parent to DetailFragment
     * provides more details per movie via id
     */

    //view for detailActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);




    }
}
