package com.jdevs.timeo.data.records

import androidx.paging.DataSource
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.db.TimeoDatabase
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.util.time.getDaysSinceEpoch
import com.jdevs.timeo.util.time.getMonthSinceEpoch
import com.jdevs.timeo.util.time.getWeeksSinceEpoch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

interface RecordsDataSource {

    suspend fun addRecord(record: Record): WriteBatch?

    suspend fun deleteRecord(record: Record): WriteBatch?
}

interface RecordsLocalDataSource : RecordsDataSource {

    val records: DataSource.Factory<Int, Record>
}

@Singleton
class RoomRecordsDataSource @Inject constructor(private val db: TimeoDatabase) :
    RecordsLocalDataSource {

    override val records by lazy { db.recordsDao().getRecords().map(DBRecord::mapToDomain) }

    override suspend fun addRecord(record: Record): WriteBatch? = withContext(Dispatchers.IO) {

        db.runInTransaction {

            launch {

                db.recordsDao().insert(record.mapToDB())
                db.activitiesDao().increaseTime(record.roomActivityId, record.time)
                registerStats(record.time, record.creationDate)
            }
        }

        null
    }

    override suspend fun deleteRecord(record: Record): WriteBatch? = withContext(Dispatchers.IO) {

        db.runInTransaction {

            launch {

                db.recordsDao().delete(record.mapToDB())
                db.activitiesDao().increaseTime(record.roomActivityId, -record.time)
                registerStats(-record.time, record.creationDate)
            }
        }

        null
    }

    private fun registerStats(time: Long, creationDate: OffsetDateTime) {

        with(db.statsDao()) {

            registerDayStats(time, creationDate.getDaysSinceEpoch())
            registerWeekStats(time, creationDate.getWeeksSinceEpoch())
            registerMonthStats(time, creationDate.getMonthSinceEpoch())
        }
    }
}
