package com.android.yaz.popularmovies.utilities;

import android.util.Log;

import com.android.yaz.popularmovies.model.MovieReview;
import com.android.yaz.popularmovies.model.PopularMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class to handle The Movie DB JSON data.
 */
public final class PopularMoviesJsonUtils {

    private final static String TAG = PopularMoviesJsonUtils.class.getSimpleName();

    /**
     * This method parses JSON result from a web response and returns an array of PopularMovies that describes
     * details of popular movies or top rated movies.
     * @param moviesJsonStr JSON response from server.
     * @return Array of PopularMovies describing movie data.
     * @throws JSONException If JSON data can't be properly parsed.
     */
    public static PopularMovie[] getSimplePopularMoviesStringsFromJson(String moviesJsonStr) throws JSONException {

        final String STATUS_CODE = "status_code";
        final String RESULTS = "results";

        final String MOVIE_ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";
        final String SYNOPSIS = "overview";
        final String USER_RATING = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String OUT_OF_RATING = "/10";

        PopularMovie[] parsedJsonStr = null;

        JSONObject popularMoviesJson = new JSONObject(moviesJsonStr);

        if(popularMoviesJson.has(STATUS_CODE)) {
            int statusCode = popularMoviesJson.getInt(STATUS_CODE);

            switch(statusCode) {
                case 1: // HTTP Status 200 Success.
                    break;
                case 3: // HTTP Status 401 Authentication failed: You do not have permissions to access the service.
                    return null;
                case 6: // HTTP Status 404 Invalid id: The pre-requisite id is invalid or not found.
                    return null;
                default: // Server probably down.
                    return null;
            }
        }

        JSONArray moviesArray = popularMoviesJson.getJSONArray(RESULTS);

        int moviesArrayLength = moviesArray.length();
        parsedJsonStr = new PopularMovie[moviesArrayLength];

        for(int i=0; i < moviesArrayLength; i++) {

            JSONObject movieJson = moviesArray.getJSONObject(i);

            PopularMovie movie = new PopularMovie(
                    movieJson.getString(MOVIE_ID),
                    movieJson.getString(ORIGINAL_TITLE),
                    BASE_POSTER_PATH + movieJson.getString(POSTER_PATH),
                    movieJson.getString(SYNOPSIS),
                    movieJson.getString(USER_RATING) + OUT_OF_RATING,
                    movieJson.getString(RELEASE_DATE).substring(0,4));

            parsedJsonStr[i] = movie;
        }

        return parsedJsonStr;
    }

    /**
     * This method parses JSON result from a web response and returns an array of Strings that describes
     * info of trailers of a movie.
     * @param trailersJsonStr JSON response from server.
     * @return Array of trailers of a movie.
     * @throws JSONException If JSON data can't be properly parsed.
     */
    public static String[] getTrailersStringsFromJson(String trailersJsonStr) throws JSONException {

        final String STATUS_CODE = "status_code";
        final String RESULTS = "results";

        final String TRAILER_KEY = "key";

        String[] parsedJsonStr = null;

        JSONObject trailersJson = new JSONObject(trailersJsonStr);

        if(trailersJson.has(STATUS_CODE)) {
            int statusCode = trailersJson.getInt(STATUS_CODE);

            switch(statusCode) {
                case 1: // HTTP Status 200 Success.
                    break;
                case 3: // HTTP Status 401 Authentication failed: You do not have permissions to access the service.
                    return null;
                case 6: // HTTP Status 404 Invalid id: The pre-requisite id is invalid or not found.
                    return null;
                default: // Server probably down.
                    return null;
            }
        }

        JSONArray trailersArray = trailersJson.getJSONArray(RESULTS);

        int trailersArrayLength = trailersArray.length();
        parsedJsonStr = new String[trailersArrayLength];

        for(int i=0; i < trailersArrayLength; i++) {

            JSONObject reviewJson = trailersArray.getJSONObject(i);
            parsedJsonStr[i] = reviewJson.getString(TRAILER_KEY);
        }

        Log.d(TAG, String.valueOf(parsedJsonStr.length));
        return parsedJsonStr;
    }

    /**
     * This method parses JSON result from a web response and returns an array of MovieReviews that describes
     * info of reviews of a movie.
     * @param reviewsJsonStr JSON response from server.
     * @return Array of reviews of a movie.
     * @throws JSONException If JSON data can't be properly parsed.
     */
    public static MovieReview[] getReviewsStringsFromJson(String reviewsJsonStr) throws JSONException {

        final String STATUS_CODE = "status_code";
        final String RESULTS = "results";

        final String MOVIE_ID = "id";
        final String AUTHOR = "author";
        final String REVIEW = "content";

        MovieReview[] parsedJsonStr = null;

        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);

        if(reviewsJson.has(STATUS_CODE)) {
            int statusCode = reviewsJson.getInt(STATUS_CODE);

            switch(statusCode) {
                case 1: // HTTP Status 200 Success.
                    break;
                case 3: // HTTP Status 401 Authentication failed: You do not have permissions to access the service.
                    return null;
                case 6: // HTTP Status 404 Invalid id: The pre-requisite id is invalid or not found.
                    return null;
                default: // Server probably down.
                    return null;
            }
        }

        String movieId = reviewsJson.getString(MOVIE_ID);
        JSONArray reviewsArray = reviewsJson.getJSONArray(RESULTS);

        int reviewsArrayLength = reviewsArray.length();
        parsedJsonStr = new MovieReview[reviewsArrayLength];

        for(int i=0; i < reviewsArrayLength; i++) {

            JSONObject reviewJson = reviewsArray.getJSONObject(i);

            MovieReview movieReview = new MovieReview(
                    movieId,
                    reviewJson.getString(AUTHOR),
                    reviewJson.getString(REVIEW));

            parsedJsonStr[i] = movieReview;
        }

        Log.d(TAG, String.valueOf(parsedJsonStr.length));
        return parsedJsonStr;
    }
}
