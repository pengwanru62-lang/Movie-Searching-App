package com.example.movieappcompose.data.remote
//解析JSON的数据类
import com.squareup.moshi.Json

// 单条搜索结果项
data class MovieSummary(
    @Json(name = "Title") val title: String?,
    @Json(name = "Year") val year: String?,
    @Json(name = "imdbID") val imdbID: String?,
    @Json(name = "Type") val type: String?,
    @Json(name = "Poster") val poster: String?
)

// 搜索响应（可能包含错误字段）
data class SearchResponse(
    @Json(name = "Search") val search: List<MovieSummary>?,
    @Json(name = "totalResults") val totalResults: String?,
    @Json(name = "Response") val response: String?,
    @Json(name = "Error") val error: String?
)

// 详情响应
data class MovieDetail(
    @Json(name = "Title") val title: String?,
    @Json(name = "Year") val year: String?,
    @Json(name = "Rated") val rated: String?,
    @Json(name = "Released") val released: String?,
    @Json(name = "Runtime") val runtime: String?,
    @Json(name = "Genre") val genre: String?,
    @Json(name = "Director") val director: String?,
    @Json(name = "Writer") val writer: String?,
    @Json(name = "Actors") val actors: String?,
    @Json(name = "Plot") val plot: String?,
    @Json(name = "Language") val language: String?,
    @Json(name = "Country") val country: String?,
    @Json(name = "Awards") val awards: String?,
    @Json(name = "Poster") val poster: String?,
    @Json(name = "imdbRating") val imdbRating: String?,
    @Json(name = "imdbID") val imdbID: String?,
    @Json(name = "Response") val response: String?,
    @Json(name = "Error") val error: String?
)


