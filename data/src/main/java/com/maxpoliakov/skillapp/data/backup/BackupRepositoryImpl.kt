package com.maxpoliakov.skillapp.data.backup

import com.google.api.client.http.ByteArrayContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(
    private val drive: Drive,
) : BackupRepository {
    override suspend fun createBackup(content: String) = withContext(Dispatchers.IO) {
        val file = File()
            .setParents(listOf("appDataFolder"))
            .setMimeType("text/plain")

        val byteArrayContent = ByteArrayContent.fromString("text/plain", content)

        drive.files().create(file, byteArrayContent).execute()
        Unit
    }

    override suspend fun getBackups() = withContext(Dispatchers.IO) {
        val theList = drive.files().list()
            .setPageSize(10)
            .setOrderBy("createdTime")
            .setSpaces("appDataFolder")

        val files = theList.execute().files
        println(files.size)
        files.map { file ->
            val stream = ByteArrayOutputStream()
            drive.files().get(file.id).executeMediaAndDownloadTo(stream)
            stream.toString()
        }
    }
}
