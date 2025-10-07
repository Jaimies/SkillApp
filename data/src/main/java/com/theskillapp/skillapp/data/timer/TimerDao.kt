package com.theskillapp.skillapp.data.timer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Query("SELECT * FROM timers")
    fun getAll(): Flow<List<DBTimer>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(timer: DBTimer): Long

    @Delete
    suspend fun delete(timer: DBTimer)
}
