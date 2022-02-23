package com.example.movies_n_chill

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.movies_n_chill.databinding.FragmentWatchedBinding
import com.example.movies_n_chill.databinding.FragmentWatchlistBinding
import com.example.movies_n_chill.db.Movie
import com.example.movies_n_chill.db.MovieRepository
import com.example.movies_n_chill.db.MoviesDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [watchlist.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class watchlist : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var movieViewModel : MovieViewModel
    private lateinit var binding: FragmentWatchlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }
    override fun onResume() {
        super.onResume()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(layoutInflater, container, false)
        val dao = MoviesDatabase.getInstance(activity!!.applicationContext!!).movieDAO
        val repository = MovieRepository(dao)
        val factory = MovieViewModelFactory(repository)
        movieViewModel= ViewModelProvider(this,factory).get(MovieViewModel::class.java)
        binding.movieModel = movieViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        binding.fab.setOnClickListener { view ->
            var fragment: Fragment = add_movie();
            val ft: FragmentTransaction =
                activity!!.getSupportFragmentManager().beginTransaction()
            ft.replace(this.id, fragment)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack("Wishlist");
            ft.commit();
        }
        return binding.root
    }

    private fun initRecyclerView(){
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext!!)
        displayMoviesList()
    }

    private fun displayMoviesList(){
        movieViewModel.wishlist.observe(viewLifecycleOwner, Observer {
            Log.i("From Watch", it.toString())
            binding.moviesRecyclerView.adapter = MoviesAdapter(it, {selectedItem: Movie -> listItemClicked(selectedItem)})
        })
    }

    private fun listItemClicked(movie: Movie){
        var options: Array<String> = arrayOf("Delete")
        if (!movie.added) options = arrayOf("Delete","Add to Libraty")
        val builder= AlertDialog.Builder(requireContext())
        builder.setNegativeButton("Cancel") { dialog, id ->
            dialog.dismiss()
        }
        builder.setTitle("${movie.title}").setItems(options) { dialog, which ->
            if(which==0){
                if (movie.added){
                    movieViewModel.updateWishlist(movie)
                }else{
                    movieViewModel.delete(movie)
                }
            }else if(which ==1){
                movieViewModel.updateAdded(movie)
            }
            else{
                dialog.dismiss()
            }
        }
            .show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment watchlist.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            watchlist().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}