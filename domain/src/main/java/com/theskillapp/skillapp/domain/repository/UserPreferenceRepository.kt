package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.GenericUri
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface UserPreferenceRepository {
    val backupExportDirectory: Flow<GenericUri?>

    fun getDayStartTime(): LocalTime

    fun setBackupExportDirectory(directory: GenericUri?)
}
