package org.meerkatdev.popularmovies.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.meerkatdev.popularmovies.data.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY id")
    LiveData<List<Movie>> loadAll();

    @Query("SELECT * FROM movie WHERE favourite = 1")
    Movie[] loadFavorites();

    @Query("DELETE FROM movie WHERE favourite = 0")
    void purgeNonFavorites();

    @Query("UPDATE movie SET favourite = :favorite WHERE movieId = :movieId")
    void setFavorite(int movieId, boolean favorite);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Movie... elements);
}
