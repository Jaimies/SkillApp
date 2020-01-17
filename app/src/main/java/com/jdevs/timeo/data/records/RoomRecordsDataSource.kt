package com.jdevs.timeo.data.records

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.db.TimeoDatabase
import com.jdevs.timeo.data.db.toLivePagedList
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.util.PagingConstants.RECORDS_PAGE_SIZE
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

    val records: LiveData<*>?

    suspend fun addRecord(record: Record): WriteBatch?

    suspend fun deleteRecord(record: Record): WriteBatch?
}

@Singleton
class RoomRecordsDataSource @Inject constructor(
    private val db: TimeoDatabase,
    private val mapper: DBRecordMapper,
    private val domainMapper: DBDomainRecordMapper
) : RecordsDataSource {

    override val records by lazy {

        db.recordsDao().getRecords().toLivePagedList(RECORDS_PAGE_SIZE, domainMapper)
    }

    override suspend fun addRecord(record: Record): WriteBatch? = withContext(Dispatchers.IO) {

        db.runInTransaction {

            launch {

                db.recordsDao().insert(mapper.map(record))
                db.activitiesDao().increaseTime(record.roomActivityId, record.time)
                registerStats(record.time, record.creationDate)
            }
        }

        null
    }

    override suspend fun deleteRecord(record: Record): WriteBatch? = withContext(Dispatchers.IO) {

        db.runInTransaction {

            launch {

                db.recordsDao().delete(mapper.map(record))
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
