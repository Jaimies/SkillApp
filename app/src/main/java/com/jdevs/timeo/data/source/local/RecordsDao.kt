package com.jdevs.timeo.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jdevs.timeo.data.Record

@Dao
interface RecordsDao {

    @Query("SELECT * FROM records ORDER BY id DESC")
    fun getRecords(): LiveData<List<Record>>

    @Insert
    suspend fun insert(record: Record)

    @Delete
    suspend fun delete(record: Record)

    @Query("DELETE FROM records")
    suspend fun deleteAll()
}