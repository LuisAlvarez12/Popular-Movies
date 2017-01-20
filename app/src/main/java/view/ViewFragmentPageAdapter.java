package view;

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
            MovieFragment fragment1 = new MovieFragment("popular");
            return fragment1;
        }else if(i==1){
            MovieFragment fragment2=new MovieFragment("top_rated");
            return fragment2;
        }else{
            MovieFragment fragment2=new MovieFragment("upcoming");
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
