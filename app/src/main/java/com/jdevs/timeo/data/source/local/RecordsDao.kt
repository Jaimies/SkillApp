package com.jdevs.timeo.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.RecordAndActivity

@Dao
interface RecordsDao {

    @Transaction
    @Query("SELECT * FROM records ORDER BY id DESC")
    fun getRecords(): LiveData<List<RecordAndActivity>>

    @Insert
    suspend fun insert(record: Record)

    @Delete
    suspend fun delete(record: Record)

    @Query("DELETE FROM records")
    suspend fun deleteAll()
}
