package com.jdevs.timeo.data.source.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jdevs.timeo.data.Record

@Dao
interface RecordsDao : BaseDao<Record> {

    @Transaction
    @Query(
        """SELECT records.*, activities.name FROM records
        LEFT JOIN activities ON activities.id = records.activityId 
        ORDER BY records.id DESC"""
    )
    fun getRecords(): DataSource.Factory<Int, Record>

    @Query("DELETE FROM records")
    suspend fun deleteAll()
}
