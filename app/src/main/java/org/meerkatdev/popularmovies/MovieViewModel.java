package org.meerkatdev.popularmovies;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.meerkatdev.popularmovies.data.AppDatabase;
import org.meerkatdev.popularmovies.data.models.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movies;
    private AppDatabase database;
    public MovieViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Retrieving the movies from the database");
        movies = database.movieDao().loadAll();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

}
