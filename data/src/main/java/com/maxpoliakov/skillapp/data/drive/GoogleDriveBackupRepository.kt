package com.maxpoliakov.skillapp.data.drive

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.ByteArrayContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.model.result.BackupUploadResult
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Provider
import com.maxpoliakov.skillapp.domain.model.result.BackupUploadResult.Failure as UploadFailure

fun BackupData.toByteArrayContent(mimeType: String = "text/plain"): ByteArrayContent {
    return ByteArrayContent.fromString(mimeType, contents)
}

class GoogleDriveBackupRepository @Inject constructor(
    private val driveProvider: Provider<Drive>,
    private val networkUtil: NetworkUtil,
    private val authRepository: AuthRepository,
) : BackupRepository {

    override suspend fun upload(data: BackupData): BackupUploadResult = withContext(Dispatchers.IO) {
        when {
            !networkUtil.isConnected -> UploadFailure.NoInternetConnection
            authRepository.currentUser == null -> UploadFailure.Unauthorized
            !authRepository.hasAppDataPermission -> UploadFailure.PermissionDenied
            else -> tryUpload(data)
        }
    }

    private fun tryUpload(data: BackupData): BackupUploadResult {
        try {
            doUpload(data)
            return BackupUploadResult.Success
        } catch (e: GoogleJsonResponseException) {
            if (quotaExceeded(e)) return UploadFailure.QuotaExceeded
            return UploadFailure.Error(e)
        } catch (e: IOException) {
            return UploadFailure.IOFailure(e)
        }
    }

    private fun doUpload(data: BackupData) {
        driveProvider.get()
            .files()
            .create(createFile(), data.toByteArrayContent())
            .execute()
    }

    private fun createFile(): File {
        return File()
            .setParents(listOf("appDataFolder"))
            .setMimeType("text/plain")
    }

    private fun quotaExceeded(e: GoogleJsonResponseException): Boolean {
        return e.details.errors.first().reason == "storageQuotaExceeded"
    }

    override suspend fun getBackups() = _getBackups(30)

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
        return _getBackups(2).firstOrNull()
    }

    override suspend fun getContents(backup: Backup): BackupData = withContext(Dispatchers.IO) {
        val stream = ByteArrayOutputStream()
        driveProvider.get().files().get(backup.id).executeMediaAndDownloadTo(stream)

        stream
            .toByteArray()
            .toString(StandardCharsets.UTF_8)
            .let(::BackupData)
    }
}
