package com.maxpoliakov.skillapp.ui.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.GenericUri
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.domain.repository.StubBackupRepository
import com.maxpoliakov.skillapp.model.LoadingState
import com.maxpoliakov.skillapp.resetThreads
import com.maxpoliakov.skillapp.setupThreads
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
    scope: CoroutineScope,
    isConfigured: Boolean = true,
) : BackupViewModel(backupRepository) {

    init {
        this.performBackupUseCase = performBackupUseCase
        this.scope = scope
    }

    override val isConfigured = flowOf(isConfigured)

    override fun onAttemptedToGoToRestoreBackupScreenWhenNotConfigured() {}
}

class StubPerformBackupUseCase(
    private val result: PerformBackupUseCase.Result = PerformBackupUseCase.Result.Success,
    private val delay: Long = 0L,
) : PerformBackupUseCase {
    override suspend fun performBackup(): PerformBackupUseCase.Result {
        delay(delay)
        return result
    }
}

class BackupViewModelTest : FunSpec({
    beforeSpec { setupThreads() }
    afterSpec { resetThreads() }

    context("lastBackupState") {
        test("becomes LoadingState.Success if no error occurs") {
            val useCase = StubPerformBackupUseCase()
            val backup = Backup(GenericUri("uri"), LocalDate.ofEpochDay(0).atStartOfDay())
            val repository = StubBackupRepository(
                getLastBackupResult = BackupRepository.Result.Success(backup)
            )
            val viewModel = TestBackupViewModel(useCase, repository, this)

            viewModel.lastBackupState.take(2).toList() shouldBe listOf(
                LoadingState.Loading,
                LoadingState.Success(backup),
            )
        }

        test("becomes LoadingState.Error if an error occurs") {
            val useCase = StubPerformBackupUseCase()
            val repository = StubBackupRepository(
                getLastBackupResult = BackupRepository.Result.Failure.PermissionDenied
            )
            val viewModel = TestBackupViewModel(useCase, repository, this)

            viewModel.lastBackupState.take(2).toList() shouldBe listOf(
                LoadingState.Loading,
                LoadingState.Error,
            )
        }
    }

    context("createBackup()") {
        test("isCreatingBackup becomes true for the duration of backup creation if this.isConfigured is true") {
            val useCase = StubPerformBackupUseCase(delay = 2)
            val repository = StubBackupRepository()
            val viewModel = TestBackupViewModel(useCase, repository, this, isConfigured = true)

            viewModel.isCreatingBackup.value shouldBe false
            val job = viewModel.createBackup()
            delay(1)
            viewModel.isCreatingBackup.value shouldBe true
            job.join()
            viewModel.isCreatingBackup.value shouldBe false
        }

        test("does nothing if not configured - this.isConfigured is false; isCreatingBackup stays false") {
            val useCase = StubPerformBackupUseCase(delay = 2)
            val repository = StubBackupRepository()
            val viewModel = TestBackupViewModel(useCase, repository, this, isConfigured = false)
            viewModel.createBackup()
            delay(1)
            viewModel.isCreatingBackup.value shouldBe false
        }

        context("backupResult becomes the value returned by PerformBackupUseCase::performBackup()") {
            withData(
                PerformBackupUseCase.Result.Success,
                PerformBackupUseCase.Result.CreationFailure(BackupCreator.Result.Failure(Throwable())),
                PerformBackupUseCase.Result.UploadFailure(BackupRepository.Result.Failure.PermissionDenied),
            ) { performBackupResult ->
                val useCase = StubPerformBackupUseCase(result = performBackupResult)
                val repository = StubBackupRepository()
                val viewModel = TestBackupViewModel(useCase, repository, this)

                viewModel.backupResult.value shouldBe null
                viewModel.createBackup().join()
                viewModel.backupResult.value shouldBe performBackupResult
            }
        }
    }
})
