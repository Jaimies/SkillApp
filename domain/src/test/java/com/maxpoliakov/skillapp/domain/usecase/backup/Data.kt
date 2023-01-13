package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import java.time.LocalDateTime

const val backupId = "id123"
val backup = Backup(backupId, LocalDateTime.now())

val backupData = BackupData("some backup contents")

val backupCreationFailure = BackupCreator.Result.Failure(Error("Error"))

val backupRestorationFailure = BackupRestorer.Result.Failure(Error("Error"))
val backupRepositoryFailure = BackupRepository.Result.Failure.Error(Error("Error"))
val backupRepositorySuccess = BackupRepository.Result.Success(Unit)

val getContentsSuccess = BackupRepository.Result.Success(backupData)

