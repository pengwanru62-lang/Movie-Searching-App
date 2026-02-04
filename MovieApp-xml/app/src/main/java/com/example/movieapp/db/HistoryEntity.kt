package com.example.movieapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey val imdbID: String,
    val title: String,
    val posterUrl: String?,
    val viewedAt: Long // 时间戳 ms
)
