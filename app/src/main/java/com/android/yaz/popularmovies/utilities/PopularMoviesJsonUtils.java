package com.android.yaz.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class to handle TheMovieDB JSON data.
 */
public final class PopularMoviesJsonUtils {

    public static String[] getSimplePopularMoviesStringsFromJson(String moviesJsonStr) throws JSONException {

        final String STATUS_CODE = "status_code";
        final String RESULTS = "results";
        final String POSTER_PATH = "poster_path";

        final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

        String[] parsedJsonStr = null;

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
        parsedJsonStr = new String[moviesArrayLength];

        for(int i=0; i < moviesArrayLength; i++) {

            JSONObject movieJson = moviesArray.getJSONObject(i);
            parsedJsonStr[i] = BASE_POSTER_PATH + movieJson.getString(POSTER_PATH);
        }

        return parsedJsonStr;
    }
}
