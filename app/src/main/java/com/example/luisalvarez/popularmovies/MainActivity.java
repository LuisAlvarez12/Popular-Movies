package com.example.luisalvarez.popularmovies;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.luisalvarez.popularmovies.fragment.DetailFragment;
import com.example.luisalvarez.popularmovies.fragment.MovieFragment;
import com.example.luisalvarez.popularmovies.view.SlidingTabLayout;
import com.example.luisalvarez.popularmovies.view.ViewFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static com.example.luisalvarez.popularmovies.R.id.fragment2;

public class MainActivity extends AppCompatActivity  {

    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    //Sliding tab names
    private CharSequence[] mTitles = {
            "Popular Now"
            ,"Top Rated"
            ,"Upcoming",
            "Favorites"};

    private boolean twoPane = false;
    private String MOVIETAG = "movie";

    private final ArrayList<String> list = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_two_pane) != null) {
            list.add("popular");
            list.add("top_rated");
            list.add("upcoming");
            list.add("favorites");
            twoPane = true;
            ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.listview_item_sort,R.id.text_sort,list);
            ListView sortSelect = (ListView)findViewById(R.id.sort_select);
            sortSelect.setAdapter(arrayAdapter);
            sortSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (twoPane) {
                        MovieFragment fragment;
                        Bundle args;
                        args = new Bundle();
                        args.putBoolean("twopane",true);
                        switch (position) {
                            case 0:
                                // In two-pane mode, show the detail view in this activity by
                                // adding or replacing the detail fragment using a
                                // fragment transaction.

                                args.putString("sortOrder", list.get(position));
                                fragment = new MovieFragment();
                                fragment.setArguments(args);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.movie_two_pane, fragment, MOVIETAG)
                                        .commit();
                                break;
                            case 1:
                                args.putString("sortOrder", list.get(position));
                                fragment = new MovieFragment();
                                fragment.setArguments(args);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.movie_two_pane, fragment, MOVIETAG)
                                        .commit();
                                break;
                            case 2:
                                args.putString("sortOrder", list.get(position));
                                fragment = new MovieFragment();
                                fragment.setArguments(args);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.movie_two_pane, fragment, MOVIETAG)
                                        .commit();
                                break;
                            case 3:
                                args.putString("sortOrder", list.get(position));
                                fragment = new MovieFragment();
                                fragment.setArguments(args);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.movie_two_pane, fragment, MOVIETAG)
                                        .commit();
                                break;
                        }
                    }
                }
            });

//            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.movie_two_pane, fragment, MOVIETAG)
//                        .commit();
//            }

            } else {
                setSlidingTabLayoutView();
            }

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


