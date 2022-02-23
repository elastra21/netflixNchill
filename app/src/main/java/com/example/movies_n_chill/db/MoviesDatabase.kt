package com.example.movies_n_chill.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database( entities = [Movie::class, Watchlist::class], version = 1)
@Database( entities = [Movie::class], version = 2)
abstract class MoviesDatabase : RoomDatabase (){
    abstract val movieDAO : MovieDAO
//    abstract val watchlistDAO : WatchlistDAO

    companion object{
        @Volatile
        private var INSTANCE : MoviesDatabase? = null
            fun getInstance(context : Context):MoviesDatabase{
                synchronized(this){
                    var instance : MoviesDatabase? = INSTANCE
                    if(instance ==null){
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            MoviesDatabase::class.java,
                            "movies_data_database"
                        ).build()
                    }
                    return instance
                }
            }
    }
}