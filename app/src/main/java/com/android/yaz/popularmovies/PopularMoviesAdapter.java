package com.android.yaz.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.yaz.popularmovies.model.PopularMovie;
import com.squareup.picasso.Picasso;

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.MoviesAdapterViewHolder>{

    private PopularMovie[] mMoviesData;
    private final ItemClickListener mOnClickListener;

    public interface ItemClickListener {
        void itemClick(PopularMovie clickedMovie);
    }

    public PopularMoviesAdapter(ItemClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.poster_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            PopularMovie movieDetails = mMoviesData[itemPosition];
            mOnClickListener.itemClick(movieDetails);
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

        PopularMovie movie = mMoviesData[position];
        String posterUrl = movie.getPosterPath();

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

    public void setPopularMoviesData(PopularMovie[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}
