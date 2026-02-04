package com.example.movieappcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcompose.data.remote.MovieDetail
import com.example.movieappcompose.data.repository.MovieRepository
import com.example.movieappcompose.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: MovieRepository) : ViewModel() {
    private val _detail = MutableStateFlow<NetworkResult<MovieDetail>?>(null)
    val detail: StateFlow<NetworkResult<MovieDetail>?> = _detail

    fun loadDetail(imdbId: String) {
        viewModelScope.launch {
            _detail.value = NetworkResult.Loading
            when (val res = repo.getMovieDetail(imdbId)) {
                is NetworkResult.Success -> {
                    _detail.value = res
                    // 写入历史（去重在 DAO 做）
                    repo.insertOrUpdateHistory(res.data)
                }
                is NetworkResult.Error -> {
                    _detail.value = res
                }
                else -> {}
            }
        }
    }
}
