package com.android.yaz.popularmovies.utilities;

import com.android.yaz.popularmovies.model.PopularMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class to handle TheMovieDB JSON data.
 */
public final class PopularMoviesJsonUtils {

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
}
