package org.meerkatdev.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import org.meerkatdev.popularmovies.utils.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        intent = getIntent();

        if (intent != null) {
            NetworkUtils.loadMoviePoster(getIfExists("movie:posterPath")).into((ImageView)findViewById(R.id.iv_movie_poster));
            ((TextView)findViewById(R.id.tv_movie_title)).setText(getIfExists("movie:title"));
            ((TextView)findViewById(R.id.tv_movie_release_date)).setText(getIfExists("movie:releaseDate"));
            ((TextView)findViewById(R.id.tv_vote_average)).setText(getIfExists("movie:voteAverage"));
            ((TextView)findViewById(R.id.tv_plot_synopsis)).setText(getIfExists("movie:overview"));
            ((TextView)findViewById(R.id.tv_popularity)).setText(getIfExists("movie:popularity"));
        }
    }

    private String getIfExists(String extraTag) {
        if (intent.hasExtra(extraTag)) {
            return intent.getStringExtra(extraTag);
        } else return "NOT_PRESENT";
    }

}
