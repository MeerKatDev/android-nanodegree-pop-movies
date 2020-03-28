package org.meerkatdev.popularmovies.data.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.meerkatdev.popularmovies.data.converters.DateConverters;

import java.text.ParseException;
import java.util.Date;

/**
 *  https://developers.themoviedb.org/3/movies/get-movie-reviews
 */
@Entity(tableName = "movie_review")
public class MovieReview {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String author;
    public String content;
    // assuming the URL from their db is valid
    public String url;

    public MovieReview(int id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    @Ignore
    public MovieReview(String _author, String _content, String _url) {
        this.author = _author;
        this.content = _content;
        this.url = _url;
    }

    public static MovieReview buildFromJson(JSONObject jsonMovieReview) throws JSONException {
        String author = jsonMovieReview.getString("author");
        String content   = jsonMovieReview.getString("content");
        String url    = jsonMovieReview.getString("url");
        return new MovieReview(author, content, url);
    }
}
