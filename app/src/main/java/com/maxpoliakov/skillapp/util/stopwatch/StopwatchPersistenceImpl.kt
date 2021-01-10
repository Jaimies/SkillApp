package com.maxpoliakov.skillapp.util.stopwatch

import android.content.SharedPreferences
import androidx.core.content.edit
import com.maxpoliakov.skillapp.shared.util.toZonedDateTimeOrNull
import com.maxpoliakov.skillapp.util.persistence.getStringPreference
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import javax.inject.Inject

class StopwatchPersistenceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : StopwatchPersistence {
    override fun getState(): StopwatchState {
        val skillId = sharedPreferences.getInt(SKILL_ID, -1)
        val startTimeString = sharedPreferences.getStringPreference(START_TIME, "")
        val startTime = startTimeString.toZonedDateTimeOrNull()
        if (startTime == null || skillId == -1)
            return Paused

        return Running(startTime, skillId)
    }

    override fun saveState(state: StopwatchState) = sharedPreferences.edit {
        if (state is Running) {
            putInt(SKILL_ID, state.skillId)
            putString(START_TIME, state.startTime.format(ISO_ZONED_DATE_TIME))
        } else {
            putInt(SKILL_ID, -1)
            putString(START_TIME, null)
        }
    }

    companion object {
        private const val SKILL_ID = "STOPWATCH_SKILL_ID"
        private const val START_TIME = "STOPWATCH_START_TIME"
    }
}
