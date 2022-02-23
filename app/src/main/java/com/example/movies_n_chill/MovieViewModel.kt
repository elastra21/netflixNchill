package com.example.movies_n_chill

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_n_chill.db.Movie
import com.example.movies_n_chill.db.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieViewModel (private val repository: MovieRepository):ViewModel(),Observable {

    val added = repository.added
    val watched = repository.watched
    val wishlist = repository.wishlist
    @Bindable
    val inputImage = MutableLiveData<String>()
    @Bindable
    val inputTitle = MutableLiveData<String>()
    @Bindable
    val inputDescription = MutableLiveData<String>()
    @Bindable
    val inputWatched = MutableLiveData<Boolean>()


    val saveOrUpdateButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
    }

    fun addToLibrary(){
        val image : String = inputImage.value!!
        val title : String = inputTitle.value!!
        val description : String = inputDescription.value!!
        val watched = false;
        val added = true;
        val wishlist = false;

        insert(Movie(id = 0,image,title,description,watched,added, wishlist))
        inputImage.value = null
        inputTitle.value = null
        inputDescription.value = null
    }

    fun addToWishList(){
        val image : String = inputImage.value!!
        val title : String = inputTitle.value!!
        val description : String = inputDescription.value!!
        val watched = false;
        val added = false;
        val wishlist = true;

        insert(Movie(id = 0,image,title,description,watched,added, wishlist))
        inputImage.value = null
        inputTitle.value = null
        inputDescription.value = null
    }

    fun saveOrUpdate(){
        val image : String = inputImage.value!!
        val title : String = inputTitle.value!!
        val description : String = inputDescription.value!!
        val watched = false;


        insert(Movie(id = 0,image,title,description,watched,false,false))
        inputImage.value = null
        inputTitle.value = null
        inputDescription.value = null
    }

    fun delete(){
        delete()
    }

    fun insert(movie: Movie): Job = viewModelScope.launch {
        repository.insert(movie)
    }

    fun update(movie: Movie): Job = viewModelScope.launch {
        repository.update(movie)
    }

    fun delete(movie: Movie): Job = viewModelScope.launch {
        repository.delete(movie)
    }

    fun updateAdded(movie: Movie): Job = viewModelScope.launch {
        repository.added(movie)
    }

    fun updateWishlist(movie: Movie): Job = viewModelScope.launch {
        repository.wishlisted(movie)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}