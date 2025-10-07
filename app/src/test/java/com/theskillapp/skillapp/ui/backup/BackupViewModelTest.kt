package com.theskillapp.skillapp.ui.backup

import com.theskillapp.skillapp.data.backup.BackupConfigurationManager
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager.Configuration
import com.theskillapp.skillapp.domain.model.Backup
import com.theskillapp.skillapp.domain.model.GenericUri
import com.theskillapp.skillapp.domain.repository.BackupCreator
import com.theskillapp.skillapp.domain.repository.BackupRepository
import com.theskillapp.skillapp.domain.repository.BackupRepository.Result
import com.theskillapp.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.theskillapp.skillapp.domain.repository.StubBackupRepository
import com.theskillapp.skillapp.domain.usecase.backup.StubPerformBackupUseCase
import com.theskillapp.skillapp.model.LoadingState
import com.theskillapp.skillapp.resetThreads
import com.theskillapp.skillapp.setupThreads
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import java.time.LocalDate

class TestBackupViewModel(
    performBackupUseCase: PerformBackupUseCase,
    backupRepository: BackupRepository,
    configurationManager: BackupConfigurationManager,
    scope: CoroutineScope,
) : BackupViewModel(backupRepository, configurationManager, performBackupUseCase) {

    init {
        this.scope = scope
    }

    override fun onAttemptedToGoToRestoreBackupScreenWhenNotConfigured(configuration: Configuration.Failure) {}
}

class StubConfigurationManager(
    configuration: Configuration = Configuration.Success(GenericUri("uri")),
): BackupConfigurationManager {
    override val configuration = flowOf(configuration)
    override fun handleException(throwable: Throwable) = Result.Failure.Error(throwable)
}

class BackupViewModelTest : FunSpec({
    beforeSpec { setupThreads() }
    afterSpec { resetThreads() }

    context("lastBackupState") {
        test("becomes LoadingState.Success if no error occurs") {
            val useCase = StubPerformBackupUseCase()
            val backup = Backup(GenericUri("uri"), LocalDate.ofEpochDay(0).atStartOfDay())
            val repository = StubBackupRepository(
                getLastBackupResult = Result.Success(backup)
            )
            val configurationManager = StubConfigurationManager()
            val viewModel = TestBackupViewModel(useCase, repository, configurationManager, this)

            viewModel.lastBackupState.take(2).toList() shouldBe listOf(
                LoadingState.Loading,
                LoadingState.Success(backup),
            )
        }

        test("becomes LoadingState.Error if an error occurs") {
            val useCase = StubPerformBackupUseCase()
            val repository = StubBackupRepository(
                getLastBackupResult = Result.Failure.Error(Exception())
            )
            val configurationManager = StubConfigurationManager()
            val viewModel = TestBackupViewModel(useCase, repository, configurationManager, this)

            viewModel.lastBackupState.take(2).toList() shouldBe listOf(
                LoadingState.Loading,
                LoadingState.Error,
            )
        }
    }

    context("createBackup()") {
        test("backup is performed and isBackupCreating becomes true for the duration of the process if BackupConfigurationManager::configuration is Configuration.Success") {
            val useCase = StubPerformBackupUseCase(delay = 2)
            val repository = StubBackupRepository()
            val configurationManager = StubConfigurationManager(Configuration.Success(GenericUri("uri")))
            val viewModel = TestBackupViewModel(useCase, repository, configurationManager, this)

            viewModel.isCreatingBackup.value shouldBe false
            val job = viewModel.createBackup()
            delay(1)
            viewModel.isCreatingBackup.value shouldBe true
            job.join()
            viewModel.isCreatingBackup.value shouldBe false
        }

        test("does nothing and isCreatingBackup stays false if BackupConfiguration::configuration is Configuration.Failure") {
            val useCase = StubPerformBackupUseCase(delay = 2)
            val repository = StubBackupRepository()
            val configurationManager = StubConfigurationManager(Configuration.Failure(Result.Failure.NotConfigured))
            val viewModel = TestBackupViewModel(useCase, repository, configurationManager, this)
            viewModel.createBackup()
            delay(1)
            viewModel.isCreatingBackup.value shouldBe false
        }

        context("backupResult becomes the value returned by PerformBackupUseCase::performBackup()") {
            withData(
                PerformBackupUseCase.Result.Success,
                PerformBackupUseCase.Result.CreationFailure(BackupCreator.Result.Failure(Exception())),
                PerformBackupUseCase.Result.UploadFailure(Result.Failure.Error(Exception())),
            ) { performBackupResult ->
                val useCase = StubPerformBackupUseCase(result = performBackupResult)
                val repository = StubBackupRepository()
                val configurationManager = StubConfigurationManager()
                val viewModel = TestBackupViewModel(useCase, repository, configurationManager, this)

                viewModel.backupResult.value shouldBe null
                viewModel.createBackup().join()
                viewModel.backupResult.value shouldBe performBackupResult
            }
        }
    }
})
