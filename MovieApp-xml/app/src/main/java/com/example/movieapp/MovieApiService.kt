package com.example.movieapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    // 搜索接口: https://www.omdbapi.com/?apikey=xxxx&s=batman&page=1
    @GET(".")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("page") page: Int
    ): MovieResponse

    // 详情接口
    @GET(".")
    fun getMovieDetail(
        @Query("apikey") apiKey: String,
        @Query("i") imdbID: String
    ): Call<MovieDetailResponse>

}
