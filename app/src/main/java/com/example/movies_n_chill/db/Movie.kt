package com.example.movies_n_chill.db
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_data_table")
data class Movie (
    @PrimaryKey(autoGenerate = true)
    val id: Int ,
    val image : String,
    val title: String,
    val description: String,
    val watched:Boolean,
    val added:Boolean,
    val wishlist:Boolean
)