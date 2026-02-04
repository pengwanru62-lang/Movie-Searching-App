package com.example.movieappcompose.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcompose.data.remote.MovieSummary
import com.example.movieappcompose.data.repository.MovieRepository
import com.example.movieappcompose.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repo: MovieRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _movies = MutableStateFlow<List<MovieSummary>>(emptyList())
    val movies: StateFlow<List<MovieSummary>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var currentPage = 1
    private var totalResults = Int.MAX_VALUE
    private var isLastPage = false
    private var isRequestInFlight = false

    fun updateQuery(q: String) {
        _query.value = q
    }

    fun searchNew() {
        // 新搜索：清空状态
        currentPage = 1
        isLastPage = false
        totalResults = Int.MAX_VALUE
        _movies.value = emptyList()
        fetchPage()
    }

    fun fetchPage() {
        val q = _query.value.trim()
        if (q.isEmpty() || isRequestInFlight || isLastPage) return

        viewModelScope.launch {
            isRequestInFlight = true
            _isLoading.value = true
            _error.value = null

            when (val res = repo.searchMovies(q, currentPage)) {
                is NetworkResult.Success -> {
                    val body = res.data
                    val newList = body.search ?: emptyList()
                    // append
                    _movies.value = _movies.value + newList
                    // OMDb 返回 totalResults 字符串
                    totalResults = body.totalResults?.toIntOrNull() ?: Int.MAX_VALUE
                    // 每页最多 10 条（OMDb 默认），判断是否最后一页
                    val loaded = _movies.value.size
                    isLastPage = loaded >= totalResults
                    currentPage++
                }
                is NetworkResult.Error -> {
                    _error.value = res.message
                }
                else -> {}
            }

            _isLoading.value = false
            isRequestInFlight = false
        }
    }
}
