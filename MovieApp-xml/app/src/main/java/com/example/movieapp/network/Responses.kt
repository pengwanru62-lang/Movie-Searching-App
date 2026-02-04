package com.example.movieapp.network

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Search") val Search: List<SearchItem>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val Response: String?,
    @SerializedName("Error") val Error: String?
)

data class SearchItem(
    @SerializedName("Title") val Title: String?,
    @SerializedName("Year") val Year: String?,
    @SerializedName("imdbID") val imdbID: String?,
    @SerializedName("Type") val Type: String?,
    @SerializedName("Poster") val Poster: String?
)

data class MovieDetailResponse(
    @SerializedName("Title") val Title: String?,
    @SerializedName("Year") val Year: String?,
    @SerializedName("Rated") val Rated: String?,
    @SerializedName("Released") val Released: String?,
    @SerializedName("Runtime") val Runtime: String?,
    @SerializedName("Genre") val Genre: String?,
    @SerializedName("Director") val Director: String?,
    @SerializedName("Writer") val Writer: String?,
    @SerializedName("Actors") val Actors: String?,
    @SerializedName("Plot") val Plot: String?,
    @SerializedName("Language") val Language: String?,
    @SerializedName("Country") val Country: String?,
    @SerializedName("Awards") val Awards: String?,
    @SerializedName("Poster") val Poster: String?,
    @SerializedName("imdbRating") val imdbRating: String?,
    @SerializedName("imdbID") val imdbID: String?,
    @SerializedName("Response") val Response: String?,
    @SerializedName("Error") val Error: String?
)
