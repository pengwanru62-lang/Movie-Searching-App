package com.example.movieapp.repo

import com.example.movieapp.db.AppDatabase
import com.example.movieapp.db.HistoryEntity
import com.example.movieapp.network.RetrofitClient
import com.example.movieapp.util.Constants
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val db: AppDatabase) {

    private val api = RetrofitClient.api

    suspend fun search(query: String, page: Int) =
        api.searchMovies(Constants.OMDB_API_KEY, query, page)

    suspend fun getDetail(imdbID: String) =
        api.getMovieDetail(Constants.OMDB_API_KEY, imdbID)

    // DB
    suspend fun saveHistory(imdbID: String, title: String, posterUrl: String?) {
        val entity = HistoryEntity(imdbID = imdbID, title = title, posterUrl = posterUrl, viewedAt = System.currentTimeMillis())
        db.historyDao().insert(entity)
    }

    fun getAllHistory(): Flow<List<HistoryEntity>> = db.historyDao().getAll()
}
