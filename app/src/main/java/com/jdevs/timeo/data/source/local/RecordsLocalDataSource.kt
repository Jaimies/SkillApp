package com.jdevs.timeo.data.source.local

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.RecordsDataSource
import com.jdevs.timeo.util.PagingConstants.RECORDS_PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordsLocalDataSource @Inject constructor(
    private val db: TimeoDatabase
) : RecordsDataSource {

    override val records by lazy {

        db.recordsDao().getRecords().toLivePagedList(RECORDS_PAGE_SIZE)
    }

    override suspend fun addRecord(record: Record): WriteBatch? = withContext(Dispatchers.IO) {

        db.runInTransaction {

            launch {

                db.recordsDao().insert(record)
                db.activitiesDao().increaseTime(record.roomActivityId, record.time)
            }
        }

        null
    }

    override suspend fun deleteRecord(record: Record): WriteBatch? = withContext(Dispatchers.IO) {

        db.runInTransaction {

            launch {

                db.recordsDao().delete(record)
                db.activitiesDao().increaseTime(record.roomActivityId, -record.time)
            }
        }

        null
    }
}
