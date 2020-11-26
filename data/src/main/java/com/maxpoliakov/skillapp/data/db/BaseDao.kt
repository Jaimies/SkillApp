package com.maxpoliakov.skillapp.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    suspend fun insert(item: T)

    @Delete
    suspend fun delete(item: T)

    @Update
    suspend fun update(item: T)
}
