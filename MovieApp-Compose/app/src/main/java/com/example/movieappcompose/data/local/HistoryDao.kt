package com.example.movieappcompose.data.local

import androidx.room.*

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY viewedAt DESC")
    suspend fun getAll(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(history: HistoryEntity)

    @Query("DELETE FROM history")
    suspend fun clearAll()
}

