package com.example.movieappcompose.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    // 搜索：s=关键字, page=1..n
    @GET(".")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("page") page: Int
    ): Response<SearchResponse>

    // 通过 imdbID 获取详情: i=tt...
    @GET(".")
    suspend fun getMovieDetail(
        @Query("apikey") apiKey: String,
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full"
    ): Response<MovieDetail>
}
