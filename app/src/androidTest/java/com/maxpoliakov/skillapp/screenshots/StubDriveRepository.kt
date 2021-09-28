package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import java.time.LocalDateTime
import javax.inject.Inject

class StubDriveRepository @Inject constructor() : DriveRepository {
    override suspend fun uploadBackup(content: String) {}
    override suspend fun getBackups() = listOf<Backup>()
    override suspend fun getLastBackup() = Backup("123abc", LocalDateTime.now())
    override suspend fun getBackupContents(backup: Backup) = ""
}
