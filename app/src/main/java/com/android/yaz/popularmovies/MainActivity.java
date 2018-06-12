package com.android.yaz.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.yaz.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView mJsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJsonResult = (TextView) findViewById(R.id.tv_search_results);

        URL url = NetworkUtils.buildUrlWithPopular();

        new tmdbQueryTask().execute(url);
    }

    private class tmdbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL tmdbSearchUrl = urls[0];

            String tmdbSearchResults = null;

            try {
                tmdbSearchResults = NetworkUtils.getResponseFromHttpUrl(tmdbSearchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tmdbSearchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!= null && !s.equals("")) {
                mJsonResult.setText(s);
            }
        }
    }
}