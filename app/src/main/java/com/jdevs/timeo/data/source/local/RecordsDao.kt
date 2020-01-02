package com.jdevs.timeo.data.source.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.RecordStats
import com.jdevs.timeo.util.getDaysSinceEpoch
import org.threeten.bp.OffsetDateTime

@Dao
interface RecordsDao : BaseDao<Record> {

    @Transaction
    suspend fun insertRecord(record: Record) {

        insert(record)
        registerRecord(record.time, OffsetDateTime.now().getDaysSinceEpoch())
    }

    @Transaction
    @Query(
        """SELECT records.*, activities.name FROM records
        LEFT JOIN activities ON activities.id = records.activityId 
        ORDER BY records.id DESC"""
    )
    fun getRecords(): DataSource.Factory<Int, Record>

    @Query(
        """INSERT OR REPLACE INTO stats (time, day) VALUES(
            COALESCE((
                (SELECT time FROM stats WHERE day = :day) + :time), 
                :time), :day)
            """
    )
    fun registerRecord(time: Long, day: Long)

    @Query("SELECT * FROM stats ORDER BY day DESC")
    fun getStats(): DataSource.Factory<Int, RecordStats>
}
