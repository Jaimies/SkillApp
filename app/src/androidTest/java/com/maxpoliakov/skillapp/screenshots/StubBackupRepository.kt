package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import java.time.LocalDateTime
import javax.inject.Inject

class StubBackupRepository @Inject constructor() : BackupRepository {
    override suspend fun upload(content: String) {}
    override suspend fun getBackups() = listOf<Backup>()
    override suspend fun getLastBackup() = Backup("123abc", LocalDateTime.now())
    override suspend fun getBackupContents(backup: Backup) = ""
}
