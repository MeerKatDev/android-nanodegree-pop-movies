package org.meerkatdev.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meerkatdev.popularmovies.data.models.Movie;
import org.meerkatdev.popularmovies.data.models.MovieRelatedVideo;
import org.meerkatdev.popularmovies.data.models.MovieReview;

import java.text.ParseException;

public final class JSONUtils {

    public static Movie[] getMovies(String jsonResponse) throws JSONException, ParseException {
        JSONObject mainObject = new JSONObject(jsonResponse);
        JSONArray jsonarray = mainObject.getJSONArray("results");
        Movie[] movies = new Movie[jsonarray.length()];
        for (int i = 0; i < jsonarray.length(); i++) {
            movies[i] = Movie.buildMovieFromJson(jsonarray.getJSONObject(i));
        }
        return movies;
    }

    public static MovieReview[] getMovieReviews(String jsonResponse) throws JSONException {
        JSONObject mainObject = new JSONObject(jsonResponse);
        JSONArray jsonarray = mainObject.getJSONArray("results");
        MovieReview[] movieReviews = new MovieReview[jsonarray.length()];
        for (int i = 0; i < jsonarray.length(); i++) {
            movieReviews[i] = MovieReview.buildFromJson(jsonarray.getJSONObject(i));
        }
        return movieReviews;
    }

    public static MovieRelatedVideo[] getMovieVideosRelated(String jsonResponse) throws JSONException {
        JSONObject mainObject = new JSONObject(jsonResponse);
        JSONArray jsonarray = mainObject.getJSONArray("results");
        MovieRelatedVideo[] movieRelatedVideos = new MovieRelatedVideo[jsonarray.length()];
        for (int i = 0; i < jsonarray.length(); i++) {
            movieRelatedVideos[i] = MovieRelatedVideo.buildFromJson(jsonarray.getJSONObject(i));
        }
        return movieRelatedVideos;
    }

}
