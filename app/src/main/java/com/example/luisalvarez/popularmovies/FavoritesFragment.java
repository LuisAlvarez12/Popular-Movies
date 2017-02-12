package com.example.luisalvarez.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luisalvarez.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import static com.example.luisalvarez.popularmovies.FavoritesFragment.COL_PROJ_THUMBNAIL;
import static com.example.luisalvarez.popularmovies.FavoritesFragment.COL_PROJ_TITLE;

/**
 * Created by luisalvarez on 2/12/17.
 */
public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks{

    public static final String[] mProjection = {
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_PLOT,
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTES,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_GENRES,
            MovieContract.MovieEntry.COLUMN_MOVIE_THUMBNAIL,
            MovieContract.MovieEntry.COLUMN_BACKDROP,
            MovieContract.MovieEntry._ID
    };

    public static final int COL_PROJ_TITLE = 0;
    public static final int COL_PROJ_PLOT = 1;
    public static final int COL_PROJ_RELEASE = 2;
    public static final int COL_PROJ_VOTES = 3;
    public static final int COL_PROJ_ID = 4;
    public static final int COL_PROJ_GENRES = 5;
    public static final int COL_PROJ_THUMBNAIL = 6;
    public static final int COL_PROJ_BACKDROP = 7;
    public static final int COL_PROJ_TABLE_ID = 8;

    private MovieAdapter mMovieAdapter;
    private int FORECAST_LOADER = 0;


    private GridView gridView;

    public FavoritesFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("dbfrag","1");
        mMovieAdapter = new MovieAdapter(getActivity(),null,0);
        Log.d("dbfrag","2");
        View rootView = inflater.inflate(R.layout.fragment_favorites_layout,container,false);
        gridView = (GridView)rootView.findViewById(R.id.grid_movie_layout_favs);
        Log.d("dbfrag","3");
        gridView.setAdapter(mMovieAdapter);
        Log.d("dbfrag","4");

        return rootView;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uriFavorites = MovieContract.MovieEntry.CONTENT_URI;
        Log.d("dbfrag","5");

        return new CursorLoader(
                getActivity(),
                uriFavorites,
                mProjection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Log.d("dbfrag","6");
        mMovieAdapter.swapCursor((Cursor)data);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMovieAdapter.swapCursor(null);
    }
}
class MovieAdapter extends CursorAdapter {

    private final String URL_POSTER_HEADER = "https://image.tmdb.org/t/p/w500";

    public class ViewHolder{
        private ImageView imgGridPosterImage;
        private TextView tvGridTitle;
        public ViewHolder(View v){
            imgGridPosterImage = (ImageView)v.findViewById(R.id.imageview_inner_grid);
            tvGridTitle = (TextView) v.findViewById(R.id.tv_inner_grid);
        }
    }


    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        Log.d("dbfrag","7");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_image_and_textview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        String title = cursor.getString(COL_PROJ_TITLE);
        String thumbnail = cursor.getString(COL_PROJ_THUMBNAIL);
        viewHolder.tvGridTitle.setText(title);
        Log.d("dbfrag","8");
        Picasso.with(context) //
                .load(URL_POSTER_HEADER + thumbnail) //
                .placeholder(R.drawable.placeholder_red) //
                .error(R.drawable.error_no_img_found) //
                .fit() //
                .into(viewHolder.imgGridPosterImage);
        Log.d("dbfrag","9");
    }
}