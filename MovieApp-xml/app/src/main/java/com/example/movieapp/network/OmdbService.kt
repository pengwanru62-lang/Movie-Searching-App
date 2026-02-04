package com.example.movieapp.network

import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbService {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("page") page: Int = 1
    ): SearchResponse

    @GET("/")
    suspend fun getMovieDetail(
        @Query("apikey") apiKey: String,
        @Query("i") imdbID: String
    ): MovieDetailResponse
}
