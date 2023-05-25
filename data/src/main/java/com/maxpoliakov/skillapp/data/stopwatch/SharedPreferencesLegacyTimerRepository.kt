package com.maxpoliakov.skillapp.data.stopwatch

import android.content.SharedPreferences
import com.maxpoliakov.skillapp.data.persistence.getStringPreference
import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.LegacyTimerRepository
import com.maxpoliakov.skillapp.shared.util.toZonedDateTimeOrNull
import javax.inject.Inject

class SharedPreferencesLegacyTimerRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LegacyTimerRepository {

    override fun getTimer(): Timer? {
        val skillId = sharedPreferences.getInt(SKILL_ID, -1)
        val groupId = sharedPreferences.getInt(GROUP_ID, -1)
        val startTimeString = sharedPreferences.getStringPreference(START_TIME, "")
        val startTime = startTimeString.toZonedDateTimeOrNull()
        if (startTime == null || skillId == -1)
            return null

        return Timer(skillId, groupId, startTime)
    }

    companion object {
        private const val SKILL_ID = "STOPWATCH_SKILL_ID"
        private const val GROUP_ID = "STOPWATCH_GROUP_ID"
        private const val START_TIME = "STOPWATCH_START_TIME"
    }
}
