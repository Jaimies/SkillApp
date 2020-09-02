package com.jdevs.timeo.data.records

import androidx.room.withTransaction
import com.jdevs.timeo.data.db.TimeoDatabase
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.RecordsRepository
import com.jdevs.timeo.shared.util.daysSinceEpoch
import com.jdevs.timeo.shared.util.monthSinceEpoch
import com.jdevs.timeo.shared.util.weeksSinceEpoch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRecordsRepository @Inject constructor(
    private val db: TimeoDatabase
) : RecordsRepository {

    override val records by lazy {
        db.recordsDao().getRecords().map(DBRecord::mapToDomain)
    }

    override suspend fun addRecord(record: Record) {
        db.withTransaction {
            db.recordsDao().insert(record.mapToDB())
            db.activitiesDao()
                .increaseTime(record.activityId, record.time)
            registerStats(record.time, record.creationDate)
        }
    }

    override suspend fun deleteRecord(record: Record) {
        db.withTransaction {
            db.recordsDao().delete(record.mapToDB())
            db.activitiesDao()
                .increaseTime(record.activityId, -record.time)
            registerStats(-record.time, record.creationDate)
        }
    }

    private fun registerStats(time: Int, creationDate: OffsetDateTime) {
        with(db.statsDao()) {
            registerDayStats(time, creationDate.daysSinceEpoch)
            registerWeekStats(time, creationDate.weeksSinceEpoch)
            registerMonthStats(time, creationDate.monthSinceEpoch)
        }
    }
}
