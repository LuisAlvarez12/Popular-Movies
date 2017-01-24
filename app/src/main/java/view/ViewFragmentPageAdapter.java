package view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.luisalvarez.popularmovies.MovieFragment;

/**
 * Created by luisalvarez on 1/20/17.
 */

public class ViewFragmentPageAdapter extends FragmentStatePagerAdapter {

    private CharSequence[] mtabs;

    public ViewFragmentPageAdapter(FragmentManager fm,CharSequence[] x) {
        super(fm);
        mtabs=x;
    }

    @Override
    public Fragment getItem(int i) {


        if(i==0){
            Bundle bundle = new Bundle();
            bundle.putString("sortOrder", "popular");
            MovieFragment fragment1 = new MovieFragment();
            fragment1.setArguments(bundle);
            return fragment1;
        }else if(i==1){
            Bundle bundle = new Bundle();
            bundle.putString("sortOrder", "top_rated");
            MovieFragment fragment2=new MovieFragment();
            fragment2.setArguments(bundle);
            return fragment2;
        }else{
            Bundle bundle = new Bundle();
            bundle.putString("sortOrder", "upcoming");
            MovieFragment fragment2 =new MovieFragment();
            fragment2.setArguments(bundle);
            return fragment2;
        }
    }

    @Override
    public int getCount() {
        return mtabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mtabs[position];
    }
}
