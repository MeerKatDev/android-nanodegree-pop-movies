package org.meerkatdev.popularmovies.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import org.meerkatdev.popularmovies.data.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie WHERE showing = 1 ORDER BY id")
    LiveData<List<Movie>> loadAll();

    @Query("SELECT * FROM movie WHERE favourite = 1 AND showing = 1")
    LiveData<List<Movie>> loadLiveFavorites();

    @Query("DELETE FROM movie WHERE favourite = 0")
    int purgeNonFavorites();

    @Query("UPDATE movie SET favourite = :favorite WHERE movieId = :movieId")
    int setFavorite(int movieId, boolean favorite);

    @Query("UPDATE movie SET showing = 0 WHERE showing = 1")
    int hideAll();

    @Query("UPDATE movie SET showing = :showing WHERE movieId IN (:movieIds)")
    int showAll(Integer[] movieIds, boolean showing);

    @Query("UPDATE movie SET showing = 1 WHERE favourite = 1")
    int showAllFavorites();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Movie... elements);

    @Transaction
    default void insertAndDeleteInTransaction(Integer[] movieIds, Movie[] movies) {
        //purgeNonFavorites(); // needed in case the list is new
        hideAll(); // same here
        insertAll(movies); // we need to ensure the movies are present
        showAll(movieIds, true);
    }
}
