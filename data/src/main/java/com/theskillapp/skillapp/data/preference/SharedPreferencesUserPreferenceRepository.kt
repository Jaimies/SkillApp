package com.theskillapp.skillapp.data.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.theskillapp.skillapp.data.extensions.getStringFlow
import com.theskillapp.skillapp.data.extensions.getStringPreference
import com.theskillapp.skillapp.domain.model.GenericUri
import com.theskillapp.skillapp.domain.repository.UserPreferenceRepository
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import javax.inject.Inject

class SharedPreferencesUserPreferenceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : UserPreferenceRepository {

    override val backupExportDirectory = sharedPreferences
        .getStringFlow(BACKUP_EXPORT_DIRECTORY_KEY)
        .map { it?.let(::GenericUri) }

    override fun getDayStartTime(): LocalTime {
        return sharedPreferences
            .getStringPreference("day_start_time", "00:00")
            .let(LocalTime::parse)
    }

    override fun setBackupExportDirectory(directory: GenericUri?) {
        sharedPreferences.edit {
            putString(BACKUP_EXPORT_DIRECTORY_KEY, directory?.uriString)
        }
    }

    companion object {
        private const val BACKUP_EXPORT_DIRECTORY_KEY = "backup_export_directory"
    }
}
