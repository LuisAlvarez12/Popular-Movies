package com.example.luisalvarez.popularmovies.view;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luisalvarez.popularmovies.R;
import com.squareup.picasso.Picasso;



/**
 * Created by luisalvarez on 2/13/17.
 */

public class MovieCursorAdapter extends CursorAdapter {

    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";
    public final int COLUMN_PROJ_MOVIE_ID = 0;
    public final int COLUMN_PROJ_MOVIE_DATE = 1;
    public final int COLUMN_PROJ_MOVIE_TITLE = 2;
    public final int COLUMN_PROJ_MOVIE_POSTER=3;

    public class ViewHolder {
        private ImageView imgGridPosterImage;
        private TextView tvGridTitle;

        public ViewHolder(View v) {
            imgGridPosterImage = (ImageView) v.findViewById(R.id.imageview_inner_grid);
            tvGridTitle = (TextView) v.findViewById(R.id.tv_inner_grid);
        }
    }

    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_image_and_textview, parent, false);
        MovieCursorAdapter.ViewHolder viewHolder = new MovieCursorAdapter.ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MovieCursorAdapter.ViewHolder viewHolder = (MovieCursorAdapter.ViewHolder) view.getTag();
        String title = cursor.getString(COLUMN_PROJ_MOVIE_TITLE);
        String thumbnail = cursor.getString(COLUMN_PROJ_MOVIE_POSTER);
        viewHolder.tvGridTitle.setText(title);
        Picasso.with(context) //
                .load(URL_POSTER_HEADER + thumbnail) //
                .placeholder(R.drawable.placeholder_red) //
                .error(R.drawable.error_no_img_found) //
                .fit() //
                .into(viewHolder.imgGridPosterImage);
    }
}