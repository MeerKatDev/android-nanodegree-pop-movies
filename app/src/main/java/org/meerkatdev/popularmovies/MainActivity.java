package org.meerkatdev.popularmovies;

import org.meerkatdev.popularmovies.adapters.MovieAdapter;
import org.meerkatdev.popularmovies.data.AppDatabase;
import org.meerkatdev.popularmovies.data.models.Movie;
import org.meerkatdev.popularmovies.utils.*;
import org.meerkatdev.popularmovies.viewmodels.MovieViewModel;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/** Rubric https://review.udacity.com/#!/rubrics/2021/view
 * OK - Movie Details layout contains a section for displaying trailer videos and user reviews.
 * OK - When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
 * OK - When a trailer is selected, app uses an Intent to launch the trailer. (to Youtube app ?)
 * OK - In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite.
 *   Tap the button on a favorite movie will unfavorite it.
 * OK - App requests for related videos for a selected movie via the /movie/{id}/videos
 *   endpoint in a background thread and displays those details when the user selects a movie.
 * OK - Database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.
 * OK - Database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.
 * ---- advanced ---
 * OK - Extend the favorites database to store the movie poster, synopsis, user rating, and release date, and display them even when offline.
 * - Implement sharing functionality to allow the user to share the first trailer’s YouTube URL from the movie details screen.
 *
 * create a DAO only for the screens
 * movieScreenDAO - only the ones showing
 * movieDAO - the ones cached (?) or does Okhttp take care of it?
 * implement the caching system for everything
 * set the layout for movies well (not one movie in the middle left)
 * implement sharing
 */
public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private AppDatabase mDb;
    MovieViewModel movieViewModel;

    private final static String TAG = MainActivity.class.getSimpleName();
    private int imageWidth = 342;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadMovieData("popular");
        setupViewModel();
        setupGridView();
        mDb = AppDatabase.getInstance(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUMING");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppExecutors.getInstance().diskIO().execute(() -> mDb.movieDao().purgeNonFavorites());
    }

    /** Here you can dynamically calculate the number of columns
     *  and the layout will adapt to the screen size and orientation
     */
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int nColumns = width / imageWidth;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }

    private void setupGridView() {
        recyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager glm = new GridLayoutManager(this, numberOfColumns());
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(0, this);
        recyclerView.setAdapter(movieAdapter);
    }

    private void loadMovieData(String criterion) {
        new FetchMovieTask().execute(criterion);

    }

    private void loadFavoriteMovies() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            mDb.movieDao().purgeNonFavorites();
        });
    }

    private void setupViewModel() {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, elements -> {
            Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
            movieAdapter.setMovieData(elements);
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Movie movie = movieAdapter.availableMovies.get(clickedItemIndex);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie:posterPath",  movie.posterPath);
        intent.putExtra("movie:title",       movie.originalTitle);
        intent.putExtra("movie:releaseDate", movie.releaseDate);
        intent.putExtra("movie:voteAverage", String.valueOf(movie.voteAverage));
        intent.putExtra("movie:overview",    movie.overview);
        intent.putExtra("movie:popularity",  String.valueOf(movie.popularity));
        intent.putExtra("movie:movieId",     String.valueOf(movie.movieId));
        intent.putExtra("movie:favourite",   movie.favourite);
        startActivity(intent);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) return null;

            URL movieRequestUrl = NetworkUtils.buildMovieRequestUrl(params[0]);
            Log.d(TAG, "Querying " + movieRequestUrl.toString());
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                return JSONUtils.getMovies(jsonResponse);
            } catch (Exception e) {
                LogUtils.handleException(TAG, e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            if(movies==null) return;
            movieAdapter.setMovieData(Arrays.asList(movies));
            AppExecutors.getInstance().diskIO().execute(() -> {
                mDb.movieDao().insertAll(movies);
            });
            super.onPostExecute(movies);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_by_highest_rated:
                loadMovieData("top_rated");
                return true;
            case R.id.order_by_most_popular:
                loadMovieData("popular");
                return true;
            case R.id.show_favourites:
                loadFavoriteMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
