package com.example.movieappcompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey val imdbId: String,
    val title: String,
    val poster: String?,
    val viewedAt: Long // 时间戳
)

