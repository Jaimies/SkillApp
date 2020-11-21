package com.jdevs.timeo.data.records

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jdevs.timeo.data.db.BaseDao

@Dao
interface RecordsDao : BaseDao<DBRecord> {
    @Transaction
    @Query(
        """SELECT records.*, activities.name as activityName FROM records
        LEFT JOIN activities ON activities.id = records.activityId 
        ORDER BY records.id DESC"""
    )
    fun getRecords(): DataSource.Factory<Int, DBRecord>
}
