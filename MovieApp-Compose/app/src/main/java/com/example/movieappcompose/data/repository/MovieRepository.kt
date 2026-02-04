package com.example.movieappcompose.data.repository

import android.content.Context
import com.example.movieappcompose.data.local.AppDatabase
import com.example.movieappcompose.data.local.HistoryEntity
import com.example.movieappcompose.data.remote.MovieDetail
import com.example.movieappcompose.data.remote.OmdbApi
import com.example.movieappcompose.data.remote.SearchResponse
import com.example.movieappcompose.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import java.util.*

class MovieRepository private constructor(context: Context) {

    private val apiKey = "ec53e7de"

    private val api: OmdbApi
    private val historyDao = AppDatabase.getInstance(context).historyDao()

    init {
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        api = retrofit.create(OmdbApi::class.java)
    }

    // 搜索指定页
    suspend fun searchMovies(query: String, page: Int): NetworkResult<SearchResponse> {
        return withContext<NetworkResult<SearchResponse>>(Dispatchers.IO) {
            try {
                val res = api.searchMovies(apiKey, query, page)
                if (res.isSuccessful) {
                    val body = res.body()
                    if (body != null && body.response == "True") {
                        NetworkResult.Success(body)
                    } else {
                        NetworkResult.Error(body?.error ?: "Unknown API error")
                    }
                } else {
                    NetworkResult.Error("HTTP ${res.code()}: ${res.message()}")
                }
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Network error")
            }
        }
    }

    // 详情
    suspend fun getMovieDetail(imdbId: String): NetworkResult<MovieDetail> {
        return withContext<NetworkResult<MovieDetail>>(Dispatchers.IO) {
            try {
                val res = api.getMovieDetail(apiKey, imdbId)
                if (res.isSuccessful) {
                    val body = res.body()
                    if (body != null && body.response == "True") {
                        NetworkResult.Success(body)
                    } else {
                        NetworkResult.Error(body?.error ?: "Unknown API error")
                    }
                } else {
                    NetworkResult.Error("HTTP ${res.code()}: ${res.message()}")
                }
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Network error")
            }
        }
    }

    // ===== Room History 操作 =====

    suspend fun insertOrUpdateHistory(detail: MovieDetail) {
        withContext<Unit>(Dispatchers.IO) {
            // 保证每部电影在历史记录中唯一：以 imdbID 作为主键
            val now = System.currentTimeMillis()
            val entity = HistoryEntity(
                imdbId = detail.imdbID ?: UUID.randomUUID().toString(),
                title = detail.title ?: "Unknown",
                poster = detail.poster,
                viewedAt = now
            )
            historyDao.insertOrUpdate(entity)
        }
    }

    suspend fun getAllHistory() = withContext<List<HistoryEntity>>(Dispatchers.IO) {
        historyDao.getAll()
    }

    suspend fun clearHistory() = withContext<Unit>(Dispatchers.IO) {
        historyDao.clearAll()
    }

    companion object {
        // 简单单例
        @Volatile
        private var instance: MovieRepository? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: MovieRepository(context.applicationContext).also { instance = it }
            }
    }
}
