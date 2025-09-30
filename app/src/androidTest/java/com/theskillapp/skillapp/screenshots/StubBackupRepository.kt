package com.theskillapp.skillapp.screenshots

import com.theskillapp.skillapp.domain.model.Backup
import com.theskillapp.skillapp.domain.model.BackupData
import com.theskillapp.skillapp.domain.model.GenericUri
import com.theskillapp.skillapp.domain.repository.BackupRepository
import com.theskillapp.skillapp.domain.repository.BackupRepository.Result
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import javax.inject.Inject

class StubBackupRepository @Inject constructor() : BackupRepository {
    override suspend fun save(data: BackupData) = Result.Success(Unit)
    override suspend fun getBackups() = Result.Success(listOf<Backup>())
    override suspend fun getLastBackup() = Result.Success(Backup(GenericUri("123abc"), LocalDateTime.now()))
    override suspend fun getContents(backup: Backup) = Result.Success(BackupData(""))
    override fun getLastBackupFlow() = flowOf<Result<Backup?>>()
}
