package org.meerkatdev.popularmovies.data.converters;

import android.util.Log;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateConverters {
    @TypeConverter
    public static java.util.Date toDate(Long timestamp) {
        return timestamp == null ? null : new java.util.Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(java.util.Date date) {
            return date == null ? null : date.getTime();
        }
    // No date elaboration needed
//    public static java.util.Date parseMovieReleaseDate(String textDate) throws ParseException {
//        Log.d("Date", textDate);
//        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(textDate);
//    }
}
