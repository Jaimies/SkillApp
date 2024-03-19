package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.GenericUri
import com.maxpoliakov.skillapp.domain.repository.UserPreferenceRepository
import kotlinx.coroutines.flow.flowOf
import java.time.LocalTime

class StubUserPreferenceRepository(
    @get:JvmName("_getDayStartTime")
    var dayStartTime: LocalTime = LocalTime.MIDNIGHT,
    exportDirectory : GenericUri? = GenericUri("content://path/to/some/directory")
) : UserPreferenceRepository {
    override val backupExportDirectory = flowOf(exportDirectory)

    override fun getDayStartTime() = dayStartTime
    override fun setBackupExportDirectory(directory: GenericUri?) {}
}
