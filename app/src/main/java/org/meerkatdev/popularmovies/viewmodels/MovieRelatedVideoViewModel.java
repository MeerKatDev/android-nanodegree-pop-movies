package org.meerkatdev.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.meerkatdev.popularmovies.data.AppDatabase;
import org.meerkatdev.popularmovies.data.models.Movie;
import org.meerkatdev.popularmovies.data.models.MovieRelatedVideo;

import java.util.List;

public class MovieRelatedVideoViewModel extends AndroidViewModel {

    private static final String TAG = MovieRelatedVideoViewModel.class.getSimpleName();

    private LiveData<List<MovieRelatedVideo>> elements;

    public MovieRelatedVideoViewModel(Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Retrieving the movie videos from the database");
        elements = database.movieRelatedVideoDao().loadAll();
    }

    public LiveData<List<MovieRelatedVideo>> getAll() {
        return elements;
    }
}