package com.example.movies_n_chill

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies_n_chill.databinding.FragmentLibraryBinding
import com.example.movies_n_chill.databinding.FragmentWatchedBinding
import com.example.movies_n_chill.db.Movie
import com.example.movies_n_chill.db.MovieRepository
import com.example.movies_n_chill.db.MoviesDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [watched.newInstance] factory method to
 * create an instance of this fragment.
 */
class watched : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var movieViewModel : MovieViewModel
    private lateinit var binding: FragmentWatchedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchedBinding.inflate(layoutInflater, container, false)
        val dao = MoviesDatabase.getInstance(activity!!.applicationContext!!).movieDAO
        val repository = MovieRepository(dao)
        val factory = MovieViewModelFactory(repository)
        movieViewModel= ViewModelProvider(this,factory).get(MovieViewModel::class.java)
        binding.movieModel = movieViewModel
        binding.lifecycleOwner = this
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView(){
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext!!)
        displayMoviesList()
    }

    private fun displayMoviesList(){
        movieViewModel.watched.observe(viewLifecycleOwner, Observer {
            Log.i("From Watch", it.toString())
            binding.moviesRecyclerView.adapter = MoviesAdapter(it, {selectedItem:Movie -> listItemClicked(selectedItem)})
        })
    }

    private fun listItemClicked(movie: Movie){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(movie.title)
        builder.setMessage("Are you sure you want to delete from Watched?")
            .setCancelable(true)
            .setPositiveButton("Confirm") { dialog, id ->
                movieViewModel.update(movie)
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
////        movieViewModel.delete(movie)
//        movieViewModel.update(movie)
//        Toast.makeText(activity!!.applicationContext!!,"Selected name is ${movie.title}", Toast.LENGTH_LONG).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment watched.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            watched().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}