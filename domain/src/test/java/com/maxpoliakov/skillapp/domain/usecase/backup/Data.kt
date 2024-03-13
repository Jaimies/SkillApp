package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.model.GenericUri
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import java.time.LocalDateTime

val backupUri = GenericUri("id123")
val backup = Backup(backupUri, LocalDateTime.now())

val backupData = BackupData("some backup contents")

val backupCreationFailure = BackupCreator.Result.Failure(Error("Error"))

val backupRestorationFailure = BackupRestorer.Result.Failure(Error("Error"))
val backupRepositoryFailure = BackupRepository.Result.Failure.Error(Error("Error"))
val backupRepositorySuccess = BackupRepository.Result.Success(Unit)

val getContentsSuccess = BackupRepository.Result.Success(backupData)

