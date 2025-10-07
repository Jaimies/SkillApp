package com.theskillapp.skillapp.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    suspend fun insert(item: T): Long

    @Insert
    suspend fun insert(items: List<T>)

    @Delete
    suspend fun delete(item: T)

    @Update
    suspend fun update(item: T)
}
