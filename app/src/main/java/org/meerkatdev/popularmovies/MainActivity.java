package org.meerkatdev.popularmovies;

import org.meerkatdev.popularmovies.adapters.MovieAdapter;
import org.meerkatdev.popularmovies.data.AppDatabase;
import org.meerkatdev.popularmovies.data.models.Movie;
import org.meerkatdev.popularmovies.utils.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private AppDatabase mDb;
    MovieViewModel movieViewModel;
    GridLayoutManager glm;
    Parcelable mListState;
    //boolean favoritesPicked = false;

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String LIST_STATE_KEY = "list-state-key";
    private int imageWidth = 342;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewModel();
        setupGridView();
        mDb = AppDatabase.getInstance(this);
        loadMovieData("popular");
        Log.d(TAG, "Creating activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resuming activity");
        if (mListState != null) {
            glm.onRestoreInstanceState(mListState);
        }
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = glm.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null)
            mListState = state.getParcelable(LIST_STATE_KEY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppExecutors.getInstance().diskIO().execute(() ->
                mDb.movieDao().purgeNonFavorites()
        );
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
        glm = new GridLayoutManager(this, numberOfColumns());
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(0, this);
        recyclerView.setAdapter(movieAdapter);
    }

    private void loadMovieData(String criterion) {
        AppExecutors.getInstance().networkIO().execute(() ->
            new FetchMovieTask().execute(criterion)
        );
    }

    private void loadFavoriteMovies() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            mDb.movieDao().purgeNonFavorites();
            mDb.movieDao().showAllFavorites();
        });
    }

    private void setupViewModel() {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, elements -> {
            Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
            movieAdapter.setMovieData(elements);
        });
        movieViewModel.getFavoriteMovies().observe(this, elements -> {
            Log.d(TAG, "Updating list of favorite movies from LiveData in ViewModel");
            movieAdapter.setMovieData(elements);
        });
    }

    private void alertOffline() {
        Toast.makeText(this, "The phone is offline", Toast.LENGTH_SHORT).show();
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

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) return null;

            // OkHTTP caches the request by asking the server every time
            // if the content changed. No need to cache data manually.
            URL movieRequestUrl = NetworkUtils.buildMovieRequestUrl(params[0]);
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                return JSONUtils.getMovies(jsonResponse);
            } catch (java.net.UnknownHostException offline) {
                AppExecutors.getInstance().mainThread().execute(MainActivity.this::alertOffline);

            } catch (Exception e) {
                LogUtils.handleException(TAG, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            if(movies==null) return;
            List<Movie> movieList = Arrays.asList(movies);
            movieAdapter.setMovieData(movieList);
            Integer[] movieIds = movieList.stream().map(a -> a.movieId).toArray(Integer[]::new);
            AppExecutors.getInstance().diskIO().execute(() -> {
                mDb.movieDao().insertAndDeleteInTransaction(movieIds, movies);
            });
            super.onPostExecute(movies);

        }
    }
}
