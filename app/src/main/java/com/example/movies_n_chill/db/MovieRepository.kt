package com.example.movies_n_chill.db

class MovieRepository(private val dao : MovieDAO) {
    val added = dao.getLibrary()
    val wishlist = dao.getWishlistMovies()
    val watched = dao.getWatchedMovies();

    suspend fun insert(movie: Movie){
        dao.insertMovie(movie)
    }

    suspend fun update(movie: Movie){
        dao.updateWatched(!movie.watched, movie.id)
    }
    suspend fun added(movie: Movie){
        dao.updateadded(!movie.added, movie.id)
    }

    suspend fun wishlisted(movie: Movie){
        dao.updateWishlist(!movie.wishlist, movie.id)
    }


    suspend fun delete(movie: Movie){
        dao.deleteMovie(movie)
    }
}