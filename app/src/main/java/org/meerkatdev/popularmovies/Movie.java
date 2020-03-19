package org.meerkatdev.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    public String posterPath;
    String originalTitle;
    String releaseDate;
    double voteAverage;
    String plotSynopsis;
    double popularity;

    private Movie(String _originalTitle, String _overview, double _voteAverage, String _posterPath, String _releaseDate, double _popularity) {
        this.originalTitle   = _originalTitle;
        this.plotSynopsis    = _overview;
        this.voteAverage     = _voteAverage;
        this.posterPath      = _posterPath;
        this.releaseDate      = _releaseDate;
        this.popularity      = _popularity;
    }

    public static Movie buildMovieFromJson(JSONObject jsonMovie) throws JSONException {
        String originalTitle = jsonMovie.getString("original_title");
        double voteAverage   = jsonMovie.getDouble("vote_average");
        String posterPath    = jsonMovie.getString("poster_path");
        String releaseDate   = jsonMovie.getString("release_date");
        String overview      = jsonMovie.getString("overview");
        double popularity    = jsonMovie.getDouble("popularity");
        return new Movie(originalTitle, overview, voteAverage, posterPath, releaseDate, popularity);
    }

}
