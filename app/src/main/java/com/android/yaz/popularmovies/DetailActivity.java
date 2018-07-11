package com.android.yaz.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yaz.popularmovies.model.PopularMovie;
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

    private final static String CLICKED_MOVIE = "clickedMovie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setupUI();
    }

    protected void setupUI() {
        Intent intentOrigin = getIntent();

        if(intentOrigin.hasExtra(CLICKED_MOVIE)) {

            PopularMovie movie = intentOrigin.getParcelableExtra(CLICKED_MOVIE);

            mMovieTitle.append(movie.getOriginalTitle());
            Picasso.with(this)
                    .load(movie.getPosterPath())
                    .into(mMoviePoster);
            mMovieSynopsis.append(movie.getSynopsis());
            mMovieRating.append(movie.getUserRating());
            mMovieReleasedDate.append(movie.getReleasedDate());
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
