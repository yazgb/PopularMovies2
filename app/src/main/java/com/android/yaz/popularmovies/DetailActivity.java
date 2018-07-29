package com.android.yaz.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.yaz.popularmovies.model.AppExecutors;
import com.android.yaz.popularmovies.model.MovieDatabase;
import com.android.yaz.popularmovies.model.MovieReview;
import com.android.yaz.popularmovies.model.PopularMovie;
import com.android.yaz.popularmovies.utilities.NetworkUtils;
import com.android.yaz.popularmovies.utilities.PopularMoviesJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailersAdapter.TrailersOnClickListener,
        LoaderManager.LoaderCallbacks{

    private final static String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.title_tv) TextView mMovieTitle;
    @BindView(R.id.poster_iv) ImageView mMoviePoster;
    @BindView(R.id.released_date_tv) TextView mMovieReleasedDate;
    @BindView(R.id.rating_tv) TextView mMovieRating;
    @BindView(R.id.synopsis_tv) TextView mMovieSynopsis;
    @BindView(R.id.recycler_view_movie_trailers) RecyclerView mTrailersRecyclerView;
    @BindView(R.id.recycler_view_movie_reviews) RecyclerView mReviewsRecyclerView;

    @BindView(R.id.pb_trailers_loading_indicator) ProgressBar mTrailersLoadingPb;
    @BindView(R.id.pb_reviews_loading_indicator) ProgressBar mReviewsLoadingPb;
    @BindView(R.id.no_trailers_message_tv) TextView mNoTrailersMessage;
    @BindView(R.id.no_reviews_message_tv) TextView mNoReviewsMessage;

    @BindView(R.id.button_favorite) Button mButton;

    private String mMovieId;
    private final static String CLICKED_MOVIE = "clickedMovie";
    private final static int TRAILERS_LOADER_ID = 1;
    private final static int REVIEWS_LOADER_ID = 2;

    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private LinearLayoutManager mLinearLayoutManagerTrailers;
    private LinearLayoutManager mLinearLayoutManagerReviews;

    private MovieDatabase mDb;
    private static boolean FAVORITE_MOVIE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mDb = MovieDatabase.getInstance(getApplicationContext());
        setUpMovieDetails();
        setUpTrailersAndReviews();
    }

    protected void setUpMovieDetails() {
        Intent intentOrigin = getIntent();

        if(intentOrigin.hasExtra(CLICKED_MOVIE)) {

            PopularMovie movie = intentOrigin.getParcelableExtra(CLICKED_MOVIE);

            mMovieId = movie.getId();
            mMovieTitle.append(movie.getOriginalTitle());
            Picasso.with(this)
                    .load(movie.getPosterPath())
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder_error)
                    .into(mMoviePoster);
            mMovieSynopsis.append(movie.getSynopsis());
            mMovieRating.append(movie.getUserRating());
            mMovieReleasedDate.append(movie.getReleasedDate());

            setupButton(mMovieId);
        }
    }

    private void setupButton(final String movieId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                PopularMovie movie = mDb.movieDao().getMovie(movieId);
                if(movie == null) {
                    FAVORITE_MOVIE = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mButton.setText(R.string.button_favorite);
                        }
                    });
                } else {
                    FAVORITE_MOVIE = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mButton.setText(R.string.button_unfavorite);
                        }
                    });
                }
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClicked();
            }
        });
    }

    protected void setUpTrailersAndReviews() {

        mLinearLayoutManagerTrailers = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLinearLayoutManagerReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mTrailersRecyclerView.setLayoutManager(mLinearLayoutManagerTrailers);
        mTrailersRecyclerView.setHasFixedSize(true);

        mReviewsRecyclerView.setLayoutManager(mLinearLayoutManagerReviews);
        mReviewsRecyclerView.setHasFixedSize(true);

        mTrailersAdapter = new TrailersAdapter(this, this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, null, this);

    }

    public void buttonClicked() {
        if(FAVORITE_MOVIE)
            showUnmarkButton();
        else
            showMarkAsFavButton();
    }

    private void showMarkAsFavButton() {
        Intent intentOrigin = getIntent();
        final PopularMovie movie = intentOrigin.getParcelableExtra(CLICKED_MOVIE);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insert(movie);
                FAVORITE_MOVIE = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mButton.setText(R.string.button_unfavorite);
                    }
                });
            }
        });
    }

    private void showUnmarkButton() {
        Intent intentOrigin = getIntent();
        final PopularMovie movie = intentOrigin.getParcelableExtra(CLICKED_MOVIE);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().delete(movie);
                FAVORITE_MOVIE = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mButton.setText(R.string.button_favorite);
                    }
                });
            }
        });
    }

    protected void showTrailersDataView() {
        mNoTrailersMessage.setVisibility(View.INVISIBLE);
        mTrailersRecyclerView.setVisibility(View.VISIBLE);
    }

    protected void showReviewsDataView() {
        mNoReviewsMessage.setVisibility(View.INVISIBLE);
        mReviewsRecyclerView.setVisibility(View.VISIBLE);
    }

    protected void showNoTrailersMessage() {
        mTrailersRecyclerView.setVisibility(View.INVISIBLE);
        mNoTrailersMessage.setVisibility(View.VISIBLE);
    }

    protected void showNoReviewsMessage() {
        mReviewsRecyclerView.setVisibility(View.INVISIBLE);
        mNoReviewsMessage.setVisibility(View.VISIBLE);
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

    @Override
    public void trailerOnClick(String trailerKey) {

        Intent intentYoutubeApp = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
        if(intentYoutubeApp.resolveActivity(getPackageManager()) != null)
            startActivity(intentYoutubeApp);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if(id == TRAILERS_LOADER_ID)
            return onCreateTrailersLoader();
        else
            return onCreateReviewsLoader();
    }

    private Loader<String[]> onCreateTrailersLoader() {
        return new AsyncTaskLoader<String[]>(this) {

            String[] mTrailersData = null;

            @Override
            protected void onStartLoading() {
                if(mTrailersData != null) {
                    deliverResult(mTrailersData);
                } else {
                    mTrailersLoadingPb.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {

                URL url = NetworkUtils.buildUrlWithMovieId(mMovieId,"videos");

                try {
                    String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    String[] jsonTrailersData = PopularMoviesJsonUtils.getTrailersStringsFromJson(jsonTrailersResponse);

                    return jsonTrailersData;

                } catch(Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String[] data) {
                mTrailersData = data;
                super.deliverResult(data);
            }
        };
    }

    private Loader<MovieReview[]> onCreateReviewsLoader() {
        return new AsyncTaskLoader<MovieReview[]>(this) {

            MovieReview[] mReviewsData = null;

            @Override
            protected void onStartLoading() {
                if(mReviewsData != null) {
                    deliverResult(mReviewsData);
                } else {
                    mReviewsLoadingPb.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public MovieReview[] loadInBackground() {
                URL url = NetworkUtils.buildUrlWithMovieId(mMovieId, "reviews");

                try {
                    String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    MovieReview[] jsonReviewsData = PopularMoviesJsonUtils.getReviewsStringsFromJson(jsonReviewsResponse);

                    return jsonReviewsData;
                } catch(Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(MovieReview[] data) {
                mReviewsData = data;
                super.deliverResult(mReviewsData);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        if(loader.getId() == TRAILERS_LOADER_ID) {
            String[] trailersData = (String[]) data;
            mTrailersLoadingPb.setVisibility(View.INVISIBLE);
            if(data != null && trailersData.length > 0 ) {
                showTrailersDataView();
                mTrailersAdapter.setTrailersData(trailersData);
            } else
                showNoTrailersMessage();
        } else {
            mReviewsLoadingPb.setVisibility(View.INVISIBLE);
            MovieReview[] reviewsData = (MovieReview[]) data;
            if(data != null && reviewsData.length > 0) {
                showReviewsDataView();
                mReviewsAdapter.setReviewsAdapter(reviewsData);
            } else
                showNoReviewsMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
