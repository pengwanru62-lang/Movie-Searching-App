package com.example.movieapp

import com.google.gson.annotations.SerializedName

// 单个电影项 - 使用小写驼峰属性并通过 SerializedName 对应 OMDb 返回的字段名
data class Movie(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Poster") val posterUrl: String,
    @SerializedName("imdbID") val imdbID: String,
    // 额外可选信息（搜索返回含 Type；评分/简介来自详情接口）
    @SerializedName("Type") val type: String? = null,
    @SerializedName("imdbRating") val rating: String? = null,
    @SerializedName("Plot") val overview: String? = null
)

// 搜索结果的外层结构，保留原来的属性名（Search/Response）以兼容现有代码
data class MovieResponse(
    @SerializedName("Search") val Search: List<Movie>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val Response: String
)
