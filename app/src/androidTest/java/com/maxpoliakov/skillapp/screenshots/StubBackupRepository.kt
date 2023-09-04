package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.BackupRepository.Result
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import java.time.LocalDateTime
import javax.inject.Inject

class StubBackupRepository @Inject constructor() : BackupRepository {
    override suspend fun save(data: BackupData) = Result.Success(Unit)
    override suspend fun getBackups() = listOf<Backup>()
    override suspend fun getLastBackup() = BackupRepository.Result.Success(Backup("123abc", LocalDateTime.now()))
    override suspend fun getContents(backup: Backup) = Result.Success(BackupData(""))
}
