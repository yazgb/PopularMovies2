package com.android.yaz.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.title_tv) TextView mMovieTitle;
    @BindView(R.id.poster_iv) ImageView mMoviePoster;
    @BindView(R.id.released_date_tv) TextView mMovieReleasedDate;
    @BindView(R.id.rating_tv) TextView mMovieRating;
    @BindView(R.id.synopsis_tv) TextView mMovieSynopsis;

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
        ButterKnife.bind(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
