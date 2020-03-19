package org.meerkatdev.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meerkatdev.popularmovies.Movie;

public final class JSONUtils {

    public static Movie[] getMovies(String jsonResponse) throws JSONException {
        JSONObject mainObject = new JSONObject(jsonResponse);
        JSONArray jsonarray = mainObject.getJSONArray("results");
        Movie[] movies = new Movie[jsonarray.length()];
        for (int i = 0; i < jsonarray.length(); i++) {
            movies[i] = Movie.buildMovieFromJson(jsonarray.getJSONObject(i));
        }
        return movies;
    }

}
