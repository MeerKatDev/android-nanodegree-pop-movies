package org.meerkatdev.popularmovies.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.meerkatdev.popularmovies.data.models.MovieRelatedVideo;
import org.meerkatdev.popularmovies.data.models.MovieReview;

import java.util.List;

@Dao
public interface MovieReviewDao {

    @Query("SELECT * FROM movie_review ORDER BY id")
    LiveData<List<MovieReview>> loadAll();


    @Query("SELECT * FROM movie_review WHERE id = :id")
    LiveData<MovieReview> loadById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MovieRelatedVideo element);
}
