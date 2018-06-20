package com.android.yaz.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.yaz.popularmovies.utilities.NetworkUtils;
import com.android.yaz.popularmovies.utilities.PopularMoviesJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PopularMoviesAdapter mPopularMoviesAdapter;
    private GridLayoutManager mGridLayoutManager;
    private ProgressBar mLoadingPb;
    private TextView mErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_popular_movies);
        mRecyclerView.setHasFixedSize(true);

        mGridLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mPopularMoviesAdapter = new PopularMoviesAdapter();

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

    private class tmdbQueryTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingPb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... urls) {
            URL tmdbSearchUrl = urls[0];

            String jsonSearchResults = null;

            try {
                jsonSearchResults = NetworkUtils.getResponseFromHttpUrl(tmdbSearchUrl);

                String[] tmdbSearchResults = PopularMoviesJsonUtils.getSimplePopularMoviesStringsFromJson(jsonSearchResults);

                return tmdbSearchResults;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] popularMoviesData) {

            mLoadingPb.setVisibility(View.INVISIBLE);

            if(popularMoviesData!= null) {
                showPopularMoviesData();
                mPopularMoviesAdapter.setPopularMoviesData(popularMoviesData);
            } else {
                showErrorMessage();
            }
        }
    }
}