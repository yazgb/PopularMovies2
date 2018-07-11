package com.android.yaz.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This class is used to communicate with The Movie DB servers.
 */
public class NetworkUtils {

    private final static String TAG = NetworkUtils.class.getSimpleName();

    private final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    /* The API Key field */
    private final static String PARAM_API_KEY = "api_key";
    private final static String apiKey = "[YOUR_API_KEY_HERE]";

    /**
     * Builds the URL used to query TheMovieDB.
     *
     * @param sortOrder The type of sort order to query movies data. It can be most popular or highest rated movies.
     * @return The URL to use to query The Movie DB server.
     */
    public static URL buildUrl(String sortOrder) {

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        Log.d(TAG, builtUri.toString());

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
