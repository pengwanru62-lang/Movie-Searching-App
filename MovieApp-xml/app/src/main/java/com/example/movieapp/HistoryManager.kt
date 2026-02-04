package com.example.movieapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HistoryManager {
    private const val PREF_NAME = "movie_history"
    private const val KEY_HISTORY = "history_list"

    fun saveMovie(context: Context, movie: Movie) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(KEY_HISTORY, "[]")

        val type = object : TypeToken<MutableList<Movie>>() {}.type
        val history: MutableList<Movie> = gson.fromJson(json, type)

        // 如果已存在相同 imdbID，先删除旧的再插入最新的
        history.removeAll { it.imdbID == movie.imdbID }
        history.add(0, movie)

        prefs.edit().putString(KEY_HISTORY, gson.toJson(history)).apply()
    }

    fun getHistory(context: Context): List<Movie> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(KEY_HISTORY, "[]")

        val type = object : TypeToken<List<Movie>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearHistory(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_HISTORY).apply()
    }
}
