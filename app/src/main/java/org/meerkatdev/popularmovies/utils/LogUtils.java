package org.meerkatdev.popularmovies.utils;

import android.util.Log;

import org.json.JSONException;
import java.io.IOException;

public class LogUtils {

    public static void handleException(String tag, Exception e) {
        String exceptionName = e.getClass().getSimpleName();
        e.printStackTrace();
        Log.d(tag, "Exception " + "[" + exceptionName + "]" + e.getMessage());
    }
}
