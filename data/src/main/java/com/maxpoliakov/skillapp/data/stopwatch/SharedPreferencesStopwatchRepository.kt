package com.maxpoliakov.skillapp.data.stopwatch

import android.content.SharedPreferences
import androidx.core.content.edit
import com.maxpoliakov.skillapp.data.persistence.getStringPreference
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.State.Paused
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.State.Running
import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.shared.util.toZonedDateTimeOrNull
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import javax.inject.Inject

class SharedPreferencesStopwatchRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : StopwatchRepository {
    override fun getState(): Stopwatch.State {
        val skillId = sharedPreferences.getInt(SKILL_ID, -1)
        val groupId = sharedPreferences.getInt(GROUP_ID, -1)
        val startTimeString = sharedPreferences.getStringPreference(START_TIME, "")
        val startTime = startTimeString.toZonedDateTimeOrNull()
        if (startTime == null || skillId == -1)
            return Paused

        return Running(startTime, skillId, groupId)
    }

    override fun saveState(state: Stopwatch.State) = sharedPreferences.edit {
        if (state is Running) {
            putInt(SKILL_ID, state.skillId)
            putInt(GROUP_ID, state.groupId)
            putString(START_TIME, state.startTime.format(ISO_ZONED_DATE_TIME))
        } else {
            putInt(SKILL_ID, -1)
            putInt(GROUP_ID, -1)
            putString(START_TIME, null)
        }
    }

    companion object {
        private const val SKILL_ID = "STOPWATCH_SKILL_ID"
        private const val GROUP_ID = "STOPWATCH_GROUP_ID"
        private const val START_TIME = "STOPWATCH_START_TIME"
    }
}
