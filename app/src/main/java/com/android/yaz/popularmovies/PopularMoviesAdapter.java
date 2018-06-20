package com.android.yaz.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.MoviesAdapterViewHolder>{

    private String[] mMoviesData;

    public PopularMoviesAdapter() {

    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.poster_image);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.popular_movie_item, parent,false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder moviesAdapterViewHolder, int position) {
        String posterUrl = mMoviesData[position];

        Picasso.with(moviesAdapterViewHolder.itemView.getContext())
                .load(posterUrl)
                .into(moviesAdapterViewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        if( mMoviesData == null)
            return 0;
        else
            return mMoviesData.length;
    }

    public void setPopularMoviesData(String[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}
