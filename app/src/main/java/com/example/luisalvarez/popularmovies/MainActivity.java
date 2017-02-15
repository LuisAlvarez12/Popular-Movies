package com.example.luisalvarez.popularmovies;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.luisalvarez.popularmovies.view.SlidingTabLayout;
import com.example.luisalvarez.popularmovies.view.ViewFragmentPageAdapter;

public class MainActivity extends AppCompatActivity  {

    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    //Sliding tab names
    private CharSequence[] mTitles = {
            "Popular Now"
            ,"Top Rated"
            ,"Upcoming",
            "Favorites"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSlidingTabLayoutView();
    }

    private void setSlidingTabLayoutView() {
        //code for Sliding Tab layout via Googles sample code
        viewPager=(ViewPager)findViewById(R.id.pageslider);
        viewPager.setAdapter(new ViewFragmentPageAdapter(getSupportFragmentManager(),mTitles));
        tabLayout = (SlidingTabLayout)findViewById(R.id.slidingtabs);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setViewPager(viewPager);
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return R.color.colorPrimary;
            }
            public int getDividerColor(int position) {
                return R.color.colorPrimary;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}


