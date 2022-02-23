package com.example.movies_n_chill

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_n_chill.databinding.MovieCellBinding
import com.example.movies_n_chill.db.Movie
import java.net.URL


class MoviesAdapter(private val movieList:List<Movie>, private val clickListener: (Movie)->Unit): RecyclerView.Adapter<MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding : MovieCellBinding = DataBindingUtil.inflate(layoutInflater,R.layout.movie_cell, parent,false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position], clickListener )
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}

class MovieViewHolder(val binding: MovieCellBinding): RecyclerView.ViewHolder(binding.root){
    fun bind( movie:Movie, clickListener: (Movie)->Unit) {
        binding.movieTitle.text = movie.title
        binding.movieDescription.text = movie.description

        if (movie.wishlist && movie.added){
            binding.added.isVisible = true;
        }
//        val url = URL(movie.image)
//        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//        binding.image.setImageBitmap(bmp)
////        binding.movieItem.setOnClickListener {
////            clickListener(movie)
////        }
        binding.deleteButton.setOnClickListener{
            clickListener(movie)
        }

    }
}