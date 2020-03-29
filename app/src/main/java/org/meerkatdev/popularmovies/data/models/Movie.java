package org.meerkatdev.popularmovies.data.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;


@Entity(tableName = "movie", indices = {@Index(value = {"movieId"}, unique = true)})
public class Movie {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String posterPath;
    public String originalTitle;
    public String releaseDate;
    public String overview;
    public double voteAverage;
    public double popularity;
    public boolean favourite;
    public boolean showing;
    public int movieId;

    public Movie(int id, String originalTitle, String overview, double voteAverage, String posterPath,
                 String releaseDate, double popularity, int movieId, boolean favourite, boolean showing) {
        this.id = id;
        commonConstructor(originalTitle, overview, voteAverage, posterPath, releaseDate, popularity, movieId, favourite, showing);
    }

    @Ignore
    public Movie(String originalTitle, String overview, double voteAverage, String posterPath,
                  String releaseDate, double popularity, int movieId, boolean favourite, boolean showing) {
        commonConstructor(originalTitle, overview, voteAverage, posterPath, releaseDate, popularity, movieId, favourite, showing);
    }

    private void commonConstructor(String _originalTitle, String _overview, double _voteAverage, String _posterPath,
                                   String _releaseDate, double _popularity, int _movieId, boolean _favourite, boolean _showing) {
        this.originalTitle   = _originalTitle;
        this.overview        = _overview;
        this.voteAverage     = _voteAverage;
        this.posterPath      = _posterPath;
        this.releaseDate     = _releaseDate;
        this.popularity      = _popularity;
        this.favourite       = _favourite;
        this.movieId         = _movieId;
        this.showing         = _showing;
    }

    // used only when taking from the API
    public static Movie buildMovieFromJson(JSONObject jsonMovie) throws JSONException {
        String originalTitle = jsonMovie.getString("original_title");
        String posterPath    = jsonMovie.getString("poster_path");
        String releaseDate   = jsonMovie.getString("release_date");
        String overview      = jsonMovie.getString("overview");

        double popularity    = jsonMovie.getDouble("popularity");
        double voteAverage   = jsonMovie.getDouble("vote_average");
        int movieId          = jsonMovie.getInt("id");

        return new Movie(originalTitle, overview, voteAverage, posterPath, releaseDate, popularity, movieId, false, true);
    }

}
