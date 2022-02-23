package com.example.movies_n_chill.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.ROLLBACK

@Dao
interface MovieDAO {
    @Insert
    suspend fun insertMovie(movie: Movie) :Long

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM movie_data_table")
    fun getAllMovies():LiveData<List<Movie>>

    @Query("UPDATE movie_data_table SET watched = :value WHERE id = :id")
    suspend fun updateWatched(value: Boolean, id: Int):Int

    @Query("UPDATE movie_data_table SET added = :value WHERE id = :id")
    suspend fun updateadded( value: Boolean, id: Int):Int

    @Query("UPDATE movie_data_table SET wishlist = :value WHERE id = :id")
    suspend fun updateWishlist( value: Boolean, id: Int):Int

    @Query("SELECT * FROM movie_data_table WHERE watched = 1")
    fun getWatchedMovies():LiveData<List<Movie>>

    @Query("SELECT * FROM movie_data_table WHERE wishlist = 1")
    fun getWishlistMovies():LiveData<List<Movie>>

    @Query("SELECT * FROM movie_data_table WHERE added = 1")
    fun getLibrary():LiveData<List<Movie>>
}

