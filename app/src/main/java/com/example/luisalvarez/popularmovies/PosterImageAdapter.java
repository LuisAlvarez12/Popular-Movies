package com.example.luisalvarez.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by luisalvarez on 1/15/17.
 */

public class PosterImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> posterUrlArray;
    private static LayoutInflater inflater=null;
    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";


    public PosterImageAdapter(Context c, ArrayList a){
        mContext=c;
        posterUrlArray=a;
        Log.d("image",""+posterUrlArray.size());
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return posterUrlArray.size();
    }
    @Override
    public Object getItem(int position) {return posterUrlArray.get(position);}
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
//        ImageView imageView = (ImageView)convertView;
//        if (imageView == null){
//            imageView=new ImageView(mContext);
//
//        }
//
//        Object image = (String)getItem(position);
//        Picasso.with(mContext) //
//                .load(URL_POSTER_HEADER + image) //
//                .placeholder(R.drawable.placeholder) //
//                .error(R.drawable.error) //
//                .fit() //
//                .into(imageView);
//        return imageView;
        Holder h = new Holder();
        Object image = (String)getItem(position);
        View rowView = inflater.inflate(R.layout.grid_image_and_textview,null);
        h.tv=(TextView)rowView.findViewById(R.id.tv_inner_grid);
        h.img=(ImageView) rowView.findViewById(R.id.imageview_inner_grid);
        h.tv.setText("Fantastic beasts and where to find them");
        Picasso.with(mContext) //
                .load(URL_POSTER_HEADER + image) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .into(h.img);
        return rowView;
    }

}
