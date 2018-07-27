package com.android.yaz.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.android.yaz.popularmovies.model.MovieDatabase;
import com.android.yaz.popularmovies.model.PopularMovie;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<PopularMovie[]> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        movies = database.movieDao().getAllMovies();
    }

    public LiveData<PopularMovie[]> getMovies() {
        return movies;
    }
}
