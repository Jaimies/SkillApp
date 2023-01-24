package com.maxpoliakov.skillapp.data.drive

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.ByteArrayContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.maxpoliakov.skillapp.data.log
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository.Result
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

fun BackupData.toByteArrayContent(mimeType: String = "text/plain"): ByteArrayContent {
    return ByteArrayContent.fromString(mimeType, contents)
}

class GoogleDriveBackupRepository @Inject constructor(
    private val driveProvider: Provider<Drive>,
    private val networkUtil: NetworkUtil,
    private val authRepository: AuthRepository,
) : BackupRepository {

    override suspend fun upload(data: BackupData): Result<Unit> {
        return tryIfAuthorized { doUpload(data) }
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

    override suspend fun getLastBackup(): Result<Backup?> = tryIfAuthorized {
        _getBackups(2).firstOrNull()
    }

    override suspend fun getContents(backup: Backup): Result<BackupData> {
        return tryIfAuthorized { doGetContents(backup) }
    }

    private fun doGetContents(backup: Backup): BackupData {
        val stream = ByteArrayOutputStream()
        driveProvider.get().files().get(backup.id).executeMediaAndDownloadTo(stream)

        return stream
            .toByteArray()
            .toString(StandardCharsets.UTF_8)
            .let(::BackupData)
    }

    private suspend fun <T> doIfAuthorized(operation: suspend () -> Result<T>) =
        withContext(Dispatchers.IO) {
            when {
                !networkUtil.isConnected -> Result.Failure.NoInternetConnection
                authRepository.currentUser == null -> Result.Failure.Unauthorized
                !authRepository.hasAppDataPermission -> Result.Failure.PermissionDenied
                else -> operation()
            }
        }

    private suspend fun <T> tryIfAuthorized(operation: suspend () -> T): Result<T> {
        return doIfAuthorized { tryOperation(operation) }
    }

    private suspend fun <T> tryOperation(operation: suspend () -> T): Result<T> {
        try {
            val result = operation()
            return Result.Success(result)
        } catch (e: GoogleJsonResponseException) {
            if (quotaExceeded(e)) return Result.Failure.QuotaExceeded
            e.log()
            return Result.Failure.Error(e)
        } catch (e: IOException) {
            e.log()
            return Result.Failure.IOFailure(e)
        }
    }
}
