package com.android.yaz.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    TextView mMovieTitle;
    ImageView mMoviePoster;
    TextView mMovieReleasedDate;
    TextView mMovieRating;
    TextView mMovieSynopsis;

    final String ID = "ID";
    final String TITLE = "TITLE";
    final String POSTER = "POSTER";
    final String SYNOPSIS = "SYNOPSIS";
    final String RATING = "RATING";
    final String RELEASED_DATE = "RELEASED_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovieTitle = (TextView) findViewById(R.id.title_tv);
        mMoviePoster = (ImageView) findViewById(R.id.poster_iv);
        mMovieReleasedDate = (TextView) findViewById(R.id.released_date_tv);
        mMovieRating = (TextView) findViewById(R.id.rating_tv);
        mMovieSynopsis = (TextView) findViewById(R.id.synopsis_tv);

        Intent intentOrigin = getIntent();

        if(intentOrigin.hasExtra(ID)) {
            String movieId = intentOrigin.getStringExtra(ID);
        }

        if(intentOrigin.hasExtra(TITLE)) {
            String movieTitle = intentOrigin.getStringExtra(TITLE);
            mMovieTitle.append(movieTitle);
        }

        if(intentOrigin.hasExtra(POSTER)) {
            String moviePoster = intentOrigin.getStringExtra(POSTER);

            Picasso.with(this)
                    .load(moviePoster)
                    .into(mMoviePoster);
        }

        if(intentOrigin.hasExtra(SYNOPSIS)) {
            String movieSynopsis = intentOrigin.getStringExtra(SYNOPSIS);
            mMovieSynopsis.append(movieSynopsis);
        }

        if(intentOrigin.hasExtra(RATING)) {
            String movieRating = intentOrigin.getStringExtra(RATING);
            mMovieRating.append(movieRating);
        }

        if(intentOrigin.hasExtra(RELEASED_DATE)) {
            String movieReleasedDate = intentOrigin.getStringExtra(RELEASED_DATE);
            mMovieReleasedDate.append(movieReleasedDate);
        }
    }
}
