package com.android.yaz.popularmovies.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.yaz.popularmovies.R;

/**
 * This class helps handle settings in Preferences.
 */
public class MoviesPreferences {

    /**
     * Returns the movies sort order currently set in Preferences. The default sort order is most popular.
     *
     * @param context Used to get the SharedPreferences.
     * @return sort order that the user set on SharedPreferences. If it it isn't been set,
     * then the default sort order is most popular.
     */
    public static String getPreferredSortOrder(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForSortOrder = context.getString(R.string.pref_sort_key);
        String defaultSortOrder = context.getString(R.string.pref_sort_most_popular);

        return sharedPreferences.getString(keyForSortOrder, defaultSortOrder);
    }
}
