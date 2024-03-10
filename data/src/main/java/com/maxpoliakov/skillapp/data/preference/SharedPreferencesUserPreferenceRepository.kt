package com.maxpoliakov.skillapp.data.preference

import android.content.SharedPreferences
import com.maxpoliakov.skillapp.data.extensions.getStringPreference
import com.maxpoliakov.skillapp.domain.repository.UserPreferenceRepository
import java.time.LocalTime
import javax.inject.Inject

class SharedPreferencesUserPreferenceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : UserPreferenceRepository {

    override fun getDayStartTime(): LocalTime {
        return sharedPreferences
            .getStringPreference("day_start_time", "00:00")
            .let(LocalTime::parse)
    }
}
