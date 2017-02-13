package com.example.luisalvarez.popularmovies.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luisalvarez.popularmovies.DetailActivity;
import com.example.luisalvarez.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luisalvarez on 1/15/17.
 */

public class PosterImageAdapter extends BaseAdapter {

    //Generate Poster images for the gridview
    private Context mContext;
    private String[] posterUrlArray=null;
    private static LayoutInflater inflater=null;
    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";

    public PosterImageAdapter(Context c, String[] a){
        mContext=c;
        posterUrlArray=a;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return posterUrlArray.length;
    }
    @Override
    public Object getItem(int position) {return posterUrlArray[position];}
    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class Holder{
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder h = new Holder();
        final List<String> list = new ArrayList<>(Arrays.asList(((String)getItem(position)).split("\\|")));
        View rowView = inflater.inflate(R.layout.grid_image_and_textview,null);
        h.tv=(TextView)rowView.findViewById(R.id.tv_inner_grid);
        h.img=(ImageView) rowView.findViewById(R.id.imageview_inner_grid);
        h.tv.setText(list.get(0));
        rowView.setTag(h);
        Picasso.with(mContext) //
                .load(URL_POSTER_HEADER + list.get(3)) //
                .placeholder(R.drawable.placeholder_red) //
                .error(R.drawable.error_no_img_found) //
                .fit() //
                .into(h.img);
        //click for detailActivity
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDetailActivity = new Intent(mContext,DetailActivity.class);
                toDetailActivity.putStringArrayListExtra("moviedata",(ArrayList<String>)list);
                mContext.startActivity(toDetailActivity);
            }
        });
        return rowView;
    }

}
