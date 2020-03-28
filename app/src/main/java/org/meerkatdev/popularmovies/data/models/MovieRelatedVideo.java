package org.meerkatdev.popularmovies.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.json.JSONException;
import org.json.JSONObject;
import org.meerkatdev.popularmovies.data.converters.VideoTypes;

/**
 *  https://developers.themoviedb.org/3/movies/get-movie-videos
 *  size Allowed Values: 360, 480, 720, 1080
 */
// TODO add the size enum
@Entity(tableName = "movie_related_video")
@TypeConverters({ VideoTypes.class })
public class MovieRelatedVideo {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String key;
    public String name;
    // assuming the URL from their is valid
    public int size;
    public String type;

    public MovieRelatedVideo(int id, String key, String name, int size, String type) {
        this.id = id;
        commonConstructor(key, name, size, type);
    }

    @Ignore
    public MovieRelatedVideo(String _key, String _name, int _size, String _type) {
        commonConstructor(_key, _name, _size, _type);
    }

    private void commonConstructor(String _key, String _name, int _size, String _type) {
        this.key = _key;
        this.name = _name;
        this.size = _size;
        this.type = _type;
    }

    public static MovieRelatedVideo buildFromJson(JSONObject jsonObject) throws JSONException {
        String key  = jsonObject.getString("key");
        String name = jsonObject.getString("name");
        int size    = jsonObject.getInt("size");
        String type = jsonObject.getString("type");
        return new MovieRelatedVideo(key, name, size, type);
    }

}


