package com.maxpoliakov.skillapp.data.backup

import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager.Configuration
import com.maxpoliakov.skillapp.data.backup.BackupRepositoryImpl.OnBackupAddedListener
import com.maxpoliakov.skillapp.data.extensions.toBackup
import com.maxpoliakov.skillapp.data.file_system.FileSystem
import com.maxpoliakov.skillapp.data.file_system.GenericFile
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// TODO: manage lifetime correctly
//@Singleton
class BackupRepositoryImpl @Inject constructor(
    private val fileSystem: FileSystem,
    private val configurationManager: BackupConfigurationManager,
) : BackupRepository {

    private val backupUpdateFlow = callbackFlow {
        val onBackupAddedListener = OnBackupAddedListener { trySend(Unit) }

        addOnBackupAddedListener(onBackupAddedListener)
        trySend(Unit)

        awaitClose { removeOnBackupAddedListener(onBackupAddedListener) }
    }

    private val lastBackupFlow = configurationManager.configuration.combine(backupUpdateFlow) { configuration, _ ->
        if (configuration is Configuration.Success) getLastBackup()
        else Result.Success(null)
    }

    private var onBackupAddedListeners = mutableListOf<OnBackupAddedListener>()

    override suspend fun save(data: BackupData): Result<Unit> {
        return tryIfConfigured { configuration -> doUpload(data, configuration) }
    }

    private fun doUpload(data: BackupData, configuration: Configuration.Success) {
        val file = fileSystem.createFile(
            parentUri = configuration.directoryUri,
            name = "",
            mimeType = "application/json",
            contents = data.contents,
        )

        onBackupAddedListeners.forEach { listener ->
            listener.onBackupAdded(file.toBackup())
        }
    }

    override suspend fun getBackups() = tryIfConfigured { configuration ->
        fileSystem
            .getChildren(configuration.directoryUri)
            .map(GenericFile::toBackup)
    }

    override suspend fun getLastBackup(): Result<Backup?> = tryIfConfigured { configuration ->
        fileSystem
            .getLastChild(configuration.directoryUri)
            ?.toBackup()
    }

    override fun getLastBackupFlow() = lastBackupFlow

    override suspend fun getContents(backup: Backup): Result<BackupData> {
        return tryIfConfigured { doGetContents(backup) }
    }

    private fun doGetContents(backup: Backup): BackupData {
        return fileSystem
            .readFile(backup.uri)
            .let(::BackupData)
    }

    private suspend fun <T> doIfConfigured(operation: suspend (Configuration.Success) -> Result<T>): Result<T> {
        val configuration = configurationManager.configuration.first()

        return when (configuration) {
            is Configuration.Success -> withContext(Dispatchers.IO) { operation(configuration) }
            is Configuration.Failure -> configuration.failure
        }
    }

    private suspend fun <T> tryIfConfigured(operation: suspend (Configuration.Success) -> T): Result<T> {
        return doIfConfigured { configuration ->
            tryOperation { operation(configuration) }
        }
    }

    private suspend fun <T> tryOperation(operation: suspend () -> T): Result<T> {
        return runCatching { operation() }
            .fold({ Result.Success(it) }) { configurationManager.handleException(it) }
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
