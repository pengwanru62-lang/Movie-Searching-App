package com.example.movieappcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcompose.data.local.HistoryEntity
import com.example.movieappcompose.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val repo: MovieRepository) : ViewModel() {
    private val _history = MutableStateFlow<List<HistoryEntity>>(emptyList())
    val history: StateFlow<List<HistoryEntity>> = _history

    fun loadHistory() {
        viewModelScope.launch {
            _history.value = repo.getAllHistory()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repo.clearHistory()
            _history.value = emptyList()
        }
    }
}
