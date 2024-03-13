package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.GenericUri
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface UserPreferenceRepository {
    val backupExportDirectory: Flow<GenericUri?>

    fun getDayStartTime(): LocalTime

    fun setBackupExportDirectory(directory: GenericUri?)
}
