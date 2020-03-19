package org.meerkatdev.popularmovies;

import org.meerkatdev.popularmovies.utils.*;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadMovieData("popular");
        setupGridView();
    }

    private void setupGridView() {
        recyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(0, this);
        recyclerView.setAdapter(movieAdapter);
    }

    private void loadMovieData(String criterion) {
        new FetchMovieTask().execute(criterion);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Movie movie = movieAdapter.availableMovies[clickedItemIndex];
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie:posterPath", movie.posterPath);
        intent.putExtra("movie:title",      movie.originalTitle);
        intent.putExtra("movie:releaseDate", movie.releaseDate);
        intent.putExtra("movie:voteAverage", String.valueOf(movie.voteAverage));
        intent.putExtra("movie:overview",    movie.plotSynopsis);
        intent.putExtra("movie:popularity", String.valueOf(movie.popularity));
        startActivity(intent);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            URL movieRequestUrl = NetworkUtils.buildGenericRequestUrl(params[0]);

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                return JSONUtils.getMovies(jsonResponse);
            } catch (IOException e) {
                LogUtils.handleIOException(TAG, e);
            } catch (JSONException e) {
                LogUtils.handleJsonException(TAG, e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            movieAdapter.setMovieData(movies);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
