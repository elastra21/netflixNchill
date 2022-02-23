package com.example.movies_n_chill

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies_n_chill.databinding.FragmentLibraryBinding
import com.example.movies_n_chill.db.Movie
import com.example.movies_n_chill.db.MovieRepository
import com.example.movies_n_chill.db.MoviesDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Library.newInstance] factory method to
 * create an instance of this fragment.
 */
class Library : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var movieViewModel : MovieViewModel

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
        binding = FragmentLibraryBinding.inflate(layoutInflater, container, false)
        val dao = MoviesDatabase.getInstance(activity!!.applicationContext!!).movieDAO
        val repository = MovieRepository(dao)
        val factory = MovieViewModelFactory(repository)
        movieViewModel= ViewModelProvider(this,factory).get(MovieViewModel::class.java)
        binding.movieModel = movieViewModel
        binding.lifecycleOwner = this
        initRecyclerView()
//        return inflater.inflate(R.layout.fragment_library, container, false)
        binding.fab.setOnClickListener { view ->
            var fragment:Fragment =  add_movie();
            val ft : FragmentTransaction = activity!!.getSupportFragmentManager().beginTransaction()
            ft.replace(this.id, fragment)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack("Library");
            ft.commit();
        }
        return binding.root
    }

    private fun initRecyclerView(){
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext!!)
        displayMoviesList()
    }

    private fun displayMoviesList(){
        movieViewModel.added.observe(viewLifecycleOwner, Observer {
            Log.i("MyTag", it.toString())
            binding.moviesRecyclerView.adapter = MoviesAdapter(it, {selectedItem:Movie -> listItemClicked(selectedItem)})
        })
    }


    private fun listItemClicked(movie: Movie){
        var options: Array<String> = arrayOf("Delete")
        if (!movie.watched) options += "Mark as Watched"
        if (!movie.wishlist) options += "Add to Wishlist"

        val builder= AlertDialog.Builder(requireContext())
        builder.setNegativeButton("Cancel") { dialog, id ->
            dialog.dismiss()
        }
        builder.setTitle("${movie.title}").setItems(options) { dialog, which ->

            when(options[which]){
                "Delete"-> {
                    movieViewModel.delete(movie)
                }
                "Mark as Watched" ->{
                    movieViewModel.update(movie)
                }
                "Add to Wishlist" ->{
                    movieViewModel.updateWishlist(movie)
                }
                else -> {
                    dialog.dismiss()
                }
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
         * @return A new instance of fragment Library.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Library().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}