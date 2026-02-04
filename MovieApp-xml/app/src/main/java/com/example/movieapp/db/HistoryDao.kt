package com.example.movieapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY viewedAt DESC")
    fun getAll(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM history")
    suspend fun clearAll()
}
