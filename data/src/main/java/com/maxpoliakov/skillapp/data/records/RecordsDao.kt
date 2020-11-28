package com.maxpoliakov.skillapp.data.records

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.maxpoliakov.skillapp.data.db.BaseDao

@Dao
interface RecordsDao : BaseDao<DBRecord> {
    @Transaction
    @Query(
        """SELECT records.*, skills.name as recordName FROM records
        LEFT JOIN skills ON skills.id = records.skillId 
        ORDER BY records.id DESC"""
    )
    fun getRecords(): DataSource.Factory<Int, DBRecord>
}
