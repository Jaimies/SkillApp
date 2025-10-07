package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.Backup
import com.theskillapp.skillapp.domain.model.BackupData
import com.theskillapp.skillapp.domain.model.GenericUri
import java.time.LocalDateTime

val backupUri = GenericUri("id123")
val backup = Backup(backupUri, LocalDateTime.now())

val backupData = BackupData("some backup contents")

val backupCreationFailure = BackupCreator.Result.Failure(Exception("Error"))

val backupRestorationFailure = BackupRestorer.Result.Failure(Exception("Error"))
val backupRepositoryFailure = BackupRepository.Result.Failure.Error(Exception("Error"))
val backupRepositorySuccess = BackupRepository.Result.Success(Unit)

val getContentsSuccess = BackupRepository.Result.Success(backupData)

