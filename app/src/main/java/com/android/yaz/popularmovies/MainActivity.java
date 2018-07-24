package com.android.yaz.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

import com.android.yaz.popularmovies.model.MoviesPreferences;
import com.android.yaz.popularmovies.model.PopularMovie;
import com.android.yaz.popularmovies.utilities.NetworkUtils;
import com.android.yaz.popularmovies.utilities.PopularMoviesJsonUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PopularMoviesAdapter.ItemClickListener,
        LoaderManager.LoaderCallbacks<PopularMovie[]>, SharedPreferences.OnSharedPreferenceChangeListener{

    @BindView(R.id.recycler_view_popular_movies) RecyclerView mRecyclerView;
    @BindView(R.id.loading_pb) ProgressBar mLoadingPb;
    @BindView(R.id.error_message_tv) TextView mErrorMessage;

    private PopularMoviesAdapter mPopularMoviesAdapter;
    private GridLayoutManager mGridLayoutManager;

    private final static String CLICKED_MOVIE = "clickedMovie";
    private final static int MOVIE_LOADER_ID = 0;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpUI();
    }

    protected void setUpUI() {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            mGridLayoutManager = new GridLayoutManager(this, 3);
        }

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPopularMoviesAdapter = new PopularMoviesAdapter(this);
        mRecyclerView.setAdapter(mPopularMoviesAdapter);

        LoaderManager.LoaderCallbacks<PopularMovie[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, bundleForLoader, callback);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    private void showMoviesDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<PopularMovie[]> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<PopularMovie[]>(this) {

            PopularMovie[] mMoviesData = null;

            @Override
            protected void onStartLoading() {
                if(mMoviesData != null) {
                    deliverResult(mMoviesData);
                } else {
                    mLoadingPb.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public PopularMovie[] loadInBackground() {

                String sortOrder = MoviesPreferences.getPreferredSortOrder(MainActivity.this);

                URL url = NetworkUtils.buildUrl(sortOrder);

                try {
                    String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(url);

                    PopularMovie[] jsonMoviesData = PopularMoviesJsonUtils.getSimplePopularMoviesStringsFromJson(jsonMoviesResponse);

                    return jsonMoviesData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(PopularMovie[] data) {
                mMoviesData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<PopularMovie[]> loader, PopularMovie[] data) {

        mLoadingPb.setVisibility(View.INVISIBLE);

        if(data!= null) {
            showMoviesDataView();
            mPopularMoviesAdapter.setPopularMoviesData(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<PopularMovie[]> loader) {

    }

    @Override
    public void itemClick(PopularMovie clickedMovie) {

        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra(CLICKED_MOVIE, clickedMovie);
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
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }

        if( id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(PREFERENCES_HAVE_BEEN_UPDATED) {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}