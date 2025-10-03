package com.theskillapp.skillapp.data.backup.shared_storage

import com.theskillapp.skillapp.data.backup.BackupConfigurationManager.Configuration
import com.theskillapp.skillapp.data.backup.shared_storage.SharedStorageBackupConfigurationManager
import com.theskillapp.skillapp.domain.model.GenericUri
import com.theskillapp.skillapp.domain.repository.BackupRepository.Result
import com.theskillapp.skillapp.domain.repository.StubUserPreferenceRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import java.io.IOException

class SharedStorageBackupConfigurationManagerTest : StringSpec({
    "configuration is Configuration.Failure(Result.Failure.NotConfigured) if UserPreference::backupExportDirectory is null" {
        val preferenceRepository = StubUserPreferenceRepository(exportDirectory = null)
        val configurationManager = SharedStorageBackupConfigurationManager(preferenceRepository)
        configurationManager.configuration.first() shouldBe Configuration.Failure(Result.Failure.NotConfigured)
    }

    "configuration is Configuration.Success(uri) if UserPreference::backupExportDirectory is not null" {
        val uri = GenericUri("protocol://some/path/")
        val preferenceRepository = StubUserPreferenceRepository(exportDirectory = uri)
        val configurationManager = SharedStorageBackupConfigurationManager(preferenceRepository)
        configurationManager.configuration.first() shouldBe Configuration.Success(uri)
    }

    "handleException() always returns Result.Failure.Error() with the original throwable" {
        val configurationManager = SharedStorageBackupConfigurationManager(StubUserPreferenceRepository())

        val notImplementedError = NotImplementedError()
        val ioException = IOException()

        configurationManager.handleException(notImplementedError) shouldBe Result.Failure.Error(notImplementedError)
        configurationManager.handleException(ioException) shouldBe Result.Failure.Error(ioException)
    }
})
