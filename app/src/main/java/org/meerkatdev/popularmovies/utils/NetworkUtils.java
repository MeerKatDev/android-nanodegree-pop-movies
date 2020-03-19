package org.meerkatdev.popularmovies.utils;

import android.net.Uri;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3";
    private static final String MOVIE_DB_POSTERS_BASE_PATH = "https://image.tmdb.org/t/p/";
    private static final String THUMBNAILS_DIMENSION = "w342";
    private static final String API_KEY_PARAM = "api_key";

    /**
     * The API KEY GOES HERE
     */
    private static final String API_KEY = "";

    /**
     * Builds the URL used to query a generic API in the Movie DB,
     * with no parameters other than the API key
     *
     * @return The URL built to be immediately sent in a request
     */
    public static URL buildGenericRequestUrl(String path) {
        String requestUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(path)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build().toString();
        URL url = null;

        try {
            url = new URL(requestUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static RequestCreator loadMoviePoster(String path) {
        String imageUri = Uri.parse(MOVIE_DB_POSTERS_BASE_PATH).buildUpon()
                .appendPath(THUMBNAILS_DIMENSION)
                .appendPath(path.substring(1)) // remove initial slash
                .build().toString();
        return Picasso.get().load(imageUri);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
