package org.meerkatdev.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.meerkatdev.popularmovies.adapters.MovieRelatedVideoAdapter;
import org.meerkatdev.popularmovies.adapters.MovieReviewAdapter;
import org.meerkatdev.popularmovies.data.AppDatabase;
import org.meerkatdev.popularmovies.data.models.MovieRelatedVideo;
import org.meerkatdev.popularmovies.data.models.MovieReview;

import org.meerkatdev.popularmovies.utils.*;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements DetailsListItemClickListener {

    private final static String TAG = DetailActivity.class.getSimpleName();

    private AppDatabase mDb;
    private Intent intent;
    private MovieReviewAdapter movieReviewAdapter;
    private MovieRelatedVideoAdapter movieRelatedVideosAdapter;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        intent = getIntent();

        if (intent == null) {
            Log.d(TAG, "Intent upon opening DetailActivity was null");
            finish();
            return;
        }

        mDb = AppDatabase.getInstance(this);

        NetworkUtils.loadMoviePoster(intent.getStringExtra("movie:posterPath")).into((ImageView)findViewById(R.id.iv_movie_poster));
        ((TextView)findViewById(R.id.tv_movie_title)).setText(intent.getStringExtra("movie:title"));
        ((TextView)findViewById(R.id.tv_movie_release_date)).setText(intent.getStringExtra("movie:releaseDate"));
        ((TextView)findViewById(R.id.tv_vote_average)).setText(intent.getStringExtra("movie:voteAverage"));
        ((TextView)findViewById(R.id.tv_plot_synopsis)).setText(intent.getStringExtra("movie:overview"));
        ((TextView)findViewById(R.id.tv_popularity)).setText(intent.getStringExtra("movie:popularity"));
        String movieId = intent.getStringExtra("movie:movieId");

        loadMovieSubData(new FetchMovieReviewsTask(), movieId, "reviews");
        loadMovieSubData(new FetchMovieRelatedVideosTask(), movieId, "videos");

        setupMovieReviewsView(0);
        setupMovieRelatedVideosView(0);
        setupFavorite(intent, Integer.parseInt(movieId));

    }

    private void setupFavorite(Intent intent, int movieId) {
        isFavorite = intent.getBooleanExtra("movie:favourite", false);
        Log.d(TAG, "getting fav " + isFavorite);
        ImageView favHeart = findViewById(R.id.iv_favorite);
        setFavoriteBackground(favHeart);
        favHeart.setOnClickListener(thisView ->
            AppExecutors.getInstance().diskIO().execute(() -> {
                isFavorite = !isFavorite;
                Log.d(TAG, "setting fav" + isFavorite);
                mDb.movieDao().setFavorite(movieId, isFavorite);
                setFavoriteBackground(favHeart);
            })
        );
    }

    private void setFavoriteBackground(ImageView favHeart) {
        favHeart.setBackground(getDrawable(isFavorite ? R.drawable.ic_favorite_24px : R.drawable.ic_favorite_border_24px));
    }

    private void setupMovieReviewsView(int noItems) {
        RecyclerView movieReviewRecyclerView = findViewById(R.id.rv_movie_reviews);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        movieReviewRecyclerView.setLayoutManager(llm);
        movieReviewRecyclerView.setHasFixedSize(true);
        movieReviewAdapter = new MovieReviewAdapter(noItems, this);
        movieReviewRecyclerView.setAdapter(movieReviewAdapter);
    }

    private void setupMovieRelatedVideosView(int noItems) {
        RecyclerView movieRelatedVideosRecyclerView = findViewById(R.id.rv_movie_related_videos);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        movieRelatedVideosRecyclerView.setLayoutManager(llm);
        movieRelatedVideosRecyclerView.setHasFixedSize(true);
        movieRelatedVideosAdapter = new MovieRelatedVideoAdapter(noItems, this);
        movieRelatedVideosRecyclerView.setAdapter(movieRelatedVideosAdapter);
    }

    private void loadMovieSubData(AsyncTask asyncTask, String movieId, String object) {
        Log.d(TAG, "Starting AsyncTask");
        asyncTask.execute(movieId, object);
    }


    private void openYoutubeVideo(String videoId) {
        Log.d(TAG, "opening" + videoId);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
        try {
            startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void openFullReview(String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRelatedVideoClick(int clickedItemIndex) {
        Log.d(TAG, "opening " + clickedItemIndex);
        MovieRelatedVideo movieRelatedVideo = movieRelatedVideosAdapter.availableElements[clickedItemIndex];
        openYoutubeVideo(movieRelatedVideo.key);
    }

    @Override
    public void onReviewClick(int clickedItemIndex) {
        MovieReview movieReview = movieReviewAdapter.availableElements[clickedItemIndex];
        openFullReview(movieReview.url);
    }

    protected class FetchMovieReviewsTask extends AsyncTask<Object, Void, MovieReview[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MovieReview[] doInBackground(Object... params) {
            if (params.length == 0) return null;
            String movieId = (String) params[0];
            String object = (String) params[1];

            URL movieRequestUrl = NetworkUtils.buildMovieRequestUrl(movieId, object);
            Log.d(TAG, "Doing request to " + movieRequestUrl.toString());

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                return JSONUtils.getMovieReviews(jsonResponse);
            } catch (Exception e) {
                LogUtils.handleException(TAG, e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieReview[] elements) {
            if (elements == null) return;
            Log.d(TAG, "Populating reviews with " + elements.length + " elements");
            movieReviewAdapter.setData(elements);
            super.onPostExecute(elements);
        }

    }


    protected class FetchMovieRelatedVideosTask extends AsyncTask<Object, Void, MovieRelatedVideo[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MovieRelatedVideo[] doInBackground(Object... params) {
            if (params.length == 0) return null;
            String movieId = (String) params[0];
            String object = (String) params[1];

            URL movieRequestUrl = NetworkUtils.buildMovieRequestUrl(movieId, object);

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                Log.d(TAG, "Doing request to " + movieRequestUrl.toString());
                return JSONUtils.getMovieVideosRelated(jsonResponse);
            } catch (Exception e) {
                LogUtils.handleException(TAG, e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieRelatedVideo[] elements) {
            if (elements == null) return;
            Log.d(TAG, "Populating related videos with " + elements.length + " elements");
            movieRelatedVideosAdapter.setData(elements);
            super.onPostExecute(elements);
        }

    }
}
