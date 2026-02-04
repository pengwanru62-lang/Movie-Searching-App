package com.example.movieappcompose.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieappcompose.data.repository.MovieRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val repo = MovieRepository.getInstance(context)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(repo) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                DetailViewModel(repo) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) ->
                HistoryViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
