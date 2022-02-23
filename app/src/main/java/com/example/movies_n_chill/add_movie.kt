package com.example.movies_n_chill

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.movies_n_chill.databinding.FragmentAddMovieBinding
import com.example.movies_n_chill.db.MovieRepository
import com.example.movies_n_chill.db.MoviesDatabase
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [add_movie.newInstance] factory method to
 * create an instance of this fragment.
 */
class add_movie : Fragment(), ServerCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAddMovieBinding
    private lateinit var movieViewModel : MovieViewModel
    private lateinit var requestQueue: RequestQueue
    lateinit var fragmentTag : String
    private lateinit var settingsDialog: Dialog

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
        val last = activity!!.supportFragmentManager.backStackEntryCount -1
        val backEntry = activity!!.supportFragmentManager.getBackStackEntryAt(last)
        fragmentTag = backEntry.name.toString()
//        Toast.makeText(activity!!.applicationContext!!,tag, Toast.LENGTH_LONG).show()
        binding = FragmentAddMovieBinding.inflate(layoutInflater, container, false)
        val dao = MoviesDatabase.getInstance(activity!!.applicationContext!!).movieDAO
        val repository = MovieRepository(dao)
        val factory = MovieViewModelFactory(repository)
        movieViewModel= ViewModelProvider(this,factory).get(MovieViewModel::class.java)
        binding.movieModel = movieViewModel
        binding.lifecycleOwner = this
        binding.searchCode.setOnClickListener { view ->
            val imdbCode = binding.imdbCode.text.toString()
            showAlert()
            requestQueue = Volley.newRequestQueue(activity!!.applicationContext!!)
            MovieApi().getTitleById(requestQueue,activity!!.applicationContext!!, this, imdbCode)
        }
        return binding.root
    }

    fun showAlert(){
        settingsDialog = Dialog(activity!!)
        settingsDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        settingsDialog.setCancelable(false)
        settingsDialog.setContentView(layoutInflater.inflate(R.layout.loading_modal,null))
        settingsDialog.show()
        settingsDialog.findViewById<LottieAnimationView>(R.id.loader)!!.setAnimation("loader_transparent.json")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment add_movie.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            add_movie().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onSuccess(result: Bundle?) {
        try {
            val jsonObject = JSONObject(result!!.getString("movie"))
            binding.imageText.setText(jsonObject.getString("image"))
            binding.descriptionText.setText(jsonObject.getString("year"))
            binding.titleText.setText(jsonObject.getString("title"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (fragmentTag === "Library") {
            movieViewModel.addToLibrary()
        }else if (fragmentTag === "Wishlist"){
            movieViewModel.addToWishList()
        }
        settingsDialog.dismiss()
        activity?.onBackPressed()

    }

    override fun onFail(message: String) {
        settingsDialog.dismiss()
        Snackbar.make(binding.content, "Invalid IMDB code try again", Snackbar.LENGTH_SHORT).show()
    }

    override fun onError( message: String) {
        settingsDialog.dismiss()
        Snackbar.make(binding.content, "Invalid IMDB code try again", Snackbar.LENGTH_SHORT).show()
    }
}