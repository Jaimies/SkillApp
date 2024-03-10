package com.maxpoliakov.skillapp.data.drive

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.ByteArrayContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.maxpoliakov.skillapp.data.drive.GoogleDriveBackupRepository.OnBackupAddedListener
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository.Result
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

fun BackupData.toByteArrayContent(mimeType: String = "text/plain"): ByteArrayContent {
    return ByteArrayContent.fromString(mimeType, contents)
}

@Singleton
class GoogleDriveBackupRepository @Inject constructor(
    private val driveProvider: Provider<Drive>,
    private val networkUtil: NetworkUtil,
    private val authRepository: AuthRepository,
) : BackupRepository {

    private val lastBackupFlow = callbackFlow {
        val onBackupAddedListener = OnBackupAddedListener { backup ->
            launch { send(Result.Success(backup)) }
        }

        val signInListener = AuthRepository.SignInListener {
            launch { send(getLastBackup()) }
        }

        val signOutListener = AuthRepository.SignOutListener {
            launch { send(Result.Success(null)) }
        }

        addOnBackupAddedListener(onBackupAddedListener)
        authRepository.addSignInListener(signInListener)
        authRepository.addSignOutListener(signOutListener)

        send(getLastBackup())

        awaitClose {
            removeOnBackupAddedListener(onBackupAddedListener)
            authRepository.removeSignInListener(signInListener)
            authRepository.removeSignOutListener(signOutListener)
        }
    }

    private var onBackupAddedListeners = mutableListOf<OnBackupAddedListener>()

    override suspend fun save(data: BackupData): Result<Unit> {
        return tryIfAuthorized { doUpload(data) }
    }

    private fun doUpload(data: BackupData) {
        driveProvider.get()
            .files()
            .create(createFile(), data.toByteArrayContent())
            .execute()

        onBackupAddedListeners.forEach { listener ->
            listener.onBackupAdded(Backup("backup-id", LocalDateTime.now()))
        }
    }

    private fun createFile(): File {
        return File()
            .setParents(listOf("appDataFolder"))
            .setMimeType("text/plain")
    }

    private fun quotaExceeded(e: GoogleJsonResponseException): Boolean {
        return e.details?.errors?.first()?.reason == "storageQuotaExceeded"
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

    override fun getLastBackupFlow() = lastBackupFlow

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

    private suspend fun <T> doIfAuthorized(operation: suspend () -> Result<T>): Result<T> {
        val user = authRepository.currentUser.first()

        return when {
            user == null -> Result.Failure.Unauthorized
            !user.hasAppDataPermission -> Result.Failure.PermissionDenied
            !networkUtil.isConnected -> Result.Failure.NoInternetConnection
            else -> withContext(Dispatchers.IO) { operation() }
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
            e.printStackTrace()
            return Result.Failure.Error(e)
        } catch (e: IOException) {
            e.printStackTrace()
            return Result.Failure.IOFailure(e)
        }
    }

    private fun addOnBackupAddedListener(listener: OnBackupAddedListener) {
        onBackupAddedListeners.add(listener)
    }

    private fun removeOnBackupAddedListener(listener: OnBackupAddedListener) {
        onBackupAddedListeners.remove(listener)
    }

    private fun interface OnBackupAddedListener {
        fun onBackupAdded(backup: Backup)
    }
}
