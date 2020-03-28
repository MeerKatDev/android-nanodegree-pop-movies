package org.meerkatdev.popularmovies.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.meerkatdev.popularmovies.data.models.MovieRelatedVideo;

import java.util.List;

@Dao
public interface MovieRelatedVideoDao {

    @Query("SELECT * FROM movie_related_video ORDER BY id")
    LiveData<List<MovieRelatedVideo>> loadAll();


    @Query("SELECT * FROM movie_related_video WHERE id = :id")
    LiveData<MovieRelatedVideo> loadById(int id);

    @Insert
    void insert(MovieRelatedVideo element);
}