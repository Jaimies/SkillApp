package com.maxpoliakov.skillapp.data.drive

import com.google.api.client.http.ByteArrayContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Provider

class DriveRepositoryImpl @Inject constructor(
    private val driveProvider: Provider<Drive>,
) : DriveRepository {
    override suspend fun uploadBackup(content: String) = withContext(Dispatchers.IO) {
        val file = File()
            .setParents(listOf("appDataFolder"))
            .setMimeType("text/plain")

        val byteArrayContent = ByteArrayContent.fromString("text/plain", content)

        driveProvider.get().files().create(file, byteArrayContent).execute()
        Unit
    }

    override suspend fun getBackups() = _getBackups(10)

    private suspend fun _getBackups(limit: Int) = withContext(Dispatchers.IO) {
        val theList = driveProvider.get().files().list()
            .setPageSize(limit)
            .setOrderBy("createdTime desc")
            .setSpaces("appDataFolder")
            .setFields("files(id, createdTime)")

        theList.execute().files.map { file ->
            val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.createdTime.value), ZoneId.systemDefault())
            Backup(file.id, date)
        }
    }

    override suspend fun getLastBackup(): Backup? {
        val backups = _getBackups(2)
        if (backups.isEmpty()) return null
        else return backups[0]
    }

    override suspend fun getBackupContents(backup: Backup): String = withContext(Dispatchers.IO) {
        val stream = ByteArrayOutputStream()
        driveProvider.get().files().get(backup.id).executeMediaAndDownloadTo(stream)
        stream.toByteArray().toString(StandardCharsets.UTF_8)
    }
}
