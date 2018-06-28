package com.android.yaz.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.yaz.popularmovies.model.PopularMovie;
import com.android.yaz.popularmovies.utilities.NetworkUtils;
import com.android.yaz.popularmovies.utilities.PopularMoviesJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements PopularMoviesAdapter.ItemClickListener{

    private RecyclerView mRecyclerView;
    private PopularMoviesAdapter mPopularMoviesAdapter;
    private GridLayoutManager mGridLayoutManager;
    private ProgressBar mLoadingPb;
    private TextView mErrorMessage;

    final String ID = "ID";
    final String TITLE = "TITLE";
    final String POSTER = "POSTER";
    final String SYNOPSIS = "SYNOPSIS";
    final String RATING = "RATING";
    final String RELEASED_DATE = "RELEASED_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_popular_movies);
        mRecyclerView.setHasFixedSize(true);

        mGridLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mPopularMoviesAdapter = new PopularMoviesAdapter(this);

        mRecyclerView.setAdapter(mPopularMoviesAdapter);

        mLoadingPb = (ProgressBar) findViewById(R.id.loading_pb);
        mErrorMessage = (TextView) findViewById(R.id.error_message_tv);

        loadPopularMovies();
    }

    private void loadPopularMovies() {

        URL url = NetworkUtils.buildUrlWithPopular();
        new tmdbQueryTask().execute(url);
    }

    private void showPopularMoviesData() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private class tmdbQueryTask extends AsyncTask<URL, Void, PopularMovie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingPb.setVisibility(View.VISIBLE);
        }

        @Override
        protected PopularMovie[] doInBackground(URL... urls) {
            URL tmdbSearchUrl = urls[0];

            String jsonSearchResults = null;

            try {
                jsonSearchResults = NetworkUtils.getResponseFromHttpUrl(tmdbSearchUrl);

                PopularMovie[] tmdbSearchResults = PopularMoviesJsonUtils.getSimplePopularMoviesStringsFromJson(jsonSearchResults);

                return tmdbSearchResults;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(PopularMovie[] popularMoviesData) {

            mLoadingPb.setVisibility(View.INVISIBLE);

            if(popularMoviesData!= null) {
                showPopularMoviesData();
                mPopularMoviesAdapter.setPopularMoviesData(popularMoviesData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public void itemClick(PopularMovie clickedMovie) {

        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);

        detailIntent.putExtra(ID,clickedMovie.getId());
        detailIntent.putExtra(TITLE,clickedMovie.getOriginalTitle());
        detailIntent.putExtra(POSTER,clickedMovie.getPosterPath());
        detailIntent.putExtra(SYNOPSIS,clickedMovie.getSynopsis());
        detailIntent.putExtra(RATING,clickedMovie.getUserRating());
        detailIntent.putExtra(RELEASED_DATE, clickedMovie.getReleasedDate());

        startActivity(detailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == R.id.action_refresh) {
            mPopularMoviesAdapter.setPopularMoviesData(null);
            loadPopularMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}