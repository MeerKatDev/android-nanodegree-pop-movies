package org.meerkatdev.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.meerkatdev.popularmovies.data.AppDatabase;
import org.meerkatdev.popularmovies.data.models.Movie;
import org.meerkatdev.popularmovies.data.models.MovieReview;

import java.util.List;

public class MovieReviewViewModel extends AndroidViewModel {

    private static final String TAG = MovieReviewViewModel.class.getSimpleName();

    private LiveData<List<MovieReview>> elements;

    public MovieReviewViewModel(Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Retrieving the movie reviews from the database");
        elements = database.movieReviewDao().loadAll();
    }

    public LiveData<List<MovieReview>> getTasks() {
        return elements;
    }
}
