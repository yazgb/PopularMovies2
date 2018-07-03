package com.android.yaz.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.pref_movies);

        PreferenceScreen prefScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();
        int count = prefScreen.getPreferenceCount();
        for(int i=0; i < count; i++) {
            Preference pref = prefScreen.getPreference(i);

            if(!(pref instanceof CheckBoxPreference)) {
                String val = sharedPreferences.getString(pref.getKey(), "");
                setPreferenceSummary(pref, val);
            }
        }
    }

    private void setPreferenceSummary(Preference preference, Object value) {

        String val = value.toString();

        if(preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(val);

            if(index >= 0 ){
                preference.setSummary(listPreference.getEntries()[index]);
            }
        } else {
            preference.setSummary(val);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if(pref != null) {
            if(!(pref instanceof CheckBoxPreference)) {
                setPreferenceSummary(pref, sharedPreferences.getString(key, ""));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}