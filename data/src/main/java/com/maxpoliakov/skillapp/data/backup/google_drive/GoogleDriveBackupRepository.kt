package com.maxpoliakov.skillapp.data.backup.google_drive

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.maxpoliakov.skillapp.data.backup.google_drive.GoogleDriveBackupRepository.OnBackupAddedListener
import com.maxpoliakov.skillapp.data.extensions.toBackup
import com.maxpoliakov.skillapp.data.file_system.FileSystemManager
import com.maxpoliakov.skillapp.data.file_system.GenericFile
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.model.GenericUri
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
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDriveBackupRepository @Inject constructor(
    private val networkUtil: NetworkUtil,
    private val authRepository: AuthRepository,
    private val fileSystemManager: FileSystemManager,
) : BackupRepository {

    private val lastBackupFlow = callbackFlow {
        val onBackupAddedListener = OnBackupAddedListener { backup ->
            trySend(Result.Success(backup))
        }

        val signInListener = AuthRepository.SignInListener {
            launch { trySend(getLastBackup()) }
        }

        val signOutListener = AuthRepository.SignOutListener {
            trySend(Result.Success(null))
        }

        addOnBackupAddedListener(onBackupAddedListener)
        authRepository.addSignInListener(signInListener)
        authRepository.addSignOutListener(signOutListener)

        trySend(getLastBackup())

        awaitClose {
            removeOnBackupAddedListener(onBackupAddedListener)
            authRepository.removeSignInListener(signInListener)
            authRepository.removeSignOutListener(signOutListener)
        }
    }

    private var onBackupAddedListeners = mutableListOf<OnBackupAddedListener>()

    private val appDataFolderUri = GenericUri("appDataFolder")

    override suspend fun save(data: BackupData): Result<Unit> {
        return tryIfAuthorized { doUpload(data) }
    }

    private fun doUpload(data: BackupData) {
        val file = fileSystemManager.createFile(appDataFolderUri, "", "application/json", data.contents)

        onBackupAddedListeners.forEach { listener ->
            listener.onBackupAdded(file.toBackup())
        }
    }

    private fun quotaExceeded(e: GoogleJsonResponseException): Boolean {
        return e.details?.errors?.first()?.reason == "storageQuotaExceeded"
    }

    override suspend fun getBackups() = _getBackups(30)

    private suspend fun _getBackups(limit: Int) = withContext(Dispatchers.IO) {
        fileSystemManager
            .getChildren(appDataFolderUri)
            .map(GenericFile::toBackup)
    }

    override suspend fun getLastBackup(): Result<Backup?> = tryIfAuthorized {
        _getBackups(2).firstOrNull()
    }

    override fun getLastBackupFlow() = lastBackupFlow

    override suspend fun getContents(backup: Backup): Result<BackupData> {
        return tryIfAuthorized { doGetContents(backup) }
    }

    private fun doGetContents(backup: Backup): BackupData {
        return fileSystemManager
            .readFile(backup.uri)
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
