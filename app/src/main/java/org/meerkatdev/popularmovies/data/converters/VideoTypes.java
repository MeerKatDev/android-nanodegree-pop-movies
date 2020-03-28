package org.meerkatdev.popularmovies.data.converters;

import android.text.TextUtils;

import androidx.room.TypeConverter;

/** from the docs:
 * Allowed Values: Trailer, Teaser, Clip, Featurette, Behind the Scenes, Bloopers
 */
public class VideoTypes {
    public enum VideoType {
        TRAILER("Trailer"),
        TEASER("Teaser"),
        FEATURETTE("Featurette"),
        BEHIND_THE_SCENES("Behind the Scenes"),
        BLOOPERS("Bloopers"),
        CLIP("Clip");

        private final String text;

        VideoType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    @TypeConverter
    public static String toString(VideoType videoType) {
        if (videoType == null) return null;
        else return videoType.toString();
    }
    @TypeConverter
    public static VideoType toSelf(String videoType) {
        if (TextUtils.isEmpty(videoType)) return VideoType.CLIP;
        else return VideoType.valueOf(videoType);
    }
}
