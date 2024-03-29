package com.maxpoliakov.skillapp.data.records

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val PAGE_SIZE = 12

private val pagingConfig = PagingConfig(
    pageSize = PAGE_SIZE,
    initialLoadSize = PAGE_SIZE,
    enablePlaceholders = false
)

@Singleton
class DBRecordsRepository @Inject constructor(
    private val recordsDao: RecordsDao
) : RecordsRepository {

    private val _records by lazy {
        Pager(config = pagingConfig) {
            recordsDao.getRecords()
        }.flow.map { data ->
            data.map { it.mapToDomain() }
        }
    }

    override fun getRecordsBySkillIds(skillIds: List<Id>): Flow<PagingData<Record>> {
        return _records.map {
            it.filter { record -> record.skillId in skillIds }
        }
    }

    override fun getLatestRecordForSkillWithId(id: Id): Flow<Record?> {
        return recordsDao.getLatestRecordForSkillWithId(id).map { record ->
            record?.mapToDomain()
        }
    }

    override suspend fun getRecord(id: Int): Record? {
        return recordsDao.getRecordById(id)?.mapToDomain()
    }

    override suspend fun addRecord(record: Record): Long {
        return recordsDao.insert(record.mapToDB())
    }

    override suspend fun deleteRecord(record: Record) {
        recordsDao.delete(record.mapToDB())
    }
}
