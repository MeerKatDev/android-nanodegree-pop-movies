package org.meerkatdev.popularmovies.utils;

import android.util.Log;

import org.json.JSONException;
import java.io.IOException;

public class LogUtils {

    public static void handleIOException(String tag, IOException e) {
        e.printStackTrace();
        Log.d(tag, "Exception while doing IO: " + e.getMessage());
    }

    public static void handleJsonException(String tag, JSONException e) {
        e.printStackTrace();
        Log.d(tag, "Exception while parsing JSON: " + e.getMessage());
    }
}
