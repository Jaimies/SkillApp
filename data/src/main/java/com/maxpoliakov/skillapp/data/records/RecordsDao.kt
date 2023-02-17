package com.maxpoliakov.skillapp.data.records

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.maxpoliakov.skillapp.data.db.BaseDao
import com.maxpoliakov.skillapp.domain.model.Id
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDao : BaseDao<DBRecord> {
    @Transaction
    @Query(
        """SELECT records.*, skills.name as recordName FROM records
        LEFT JOIN skills ON skills.id = records.skillId 
        ORDER BY records.date DESC, records.id DESC"""
    )
    fun getRecords(): PagingSource<Int, DBRecord>

    @Query("SELECT * FROM records")
    suspend fun getAllRecords(): List<DBRecord>

    @Query("SELECT * FROM records WHERE id = :id")
    suspend fun getRecordById(id: Int): DBRecord?

    @Query("SELECT * FROM records WHERE skillId = :skillId ORDER BY id DESC LIMIT 1")
    fun getLatestRecordForSkillWithId(skillId: Id): Flow<DBRecord?>

    @Query("DELETE FROM records")
    suspend fun deleteAll()
}
