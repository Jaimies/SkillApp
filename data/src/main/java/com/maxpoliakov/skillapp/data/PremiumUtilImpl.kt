package com.maxpoliakov.skillapp.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.maxpoliakov.skillapp.domain.repository.PremiumUtil
import com.maxpoliakov.skillapp.shared.util.dateTimeFormatter
import java.time.LocalDateTime
import javax.inject.Inject

class PremiumUtilImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : PremiumUtil {

    override fun enableFreePremium() {
        sharedPreferences.edit {
            putString(
                FREE_PREMIUM_PERIOD_START,
                dateTimeFormatter.format(LocalDateTime.now())
            )
        }
    }

    override fun isFreePremiumAvailable(): Boolean {
        val freePremiumDateStartString = sharedPreferences.getString(FREE_PREMIUM_PERIOD_START, "")
        if (freePremiumDateStartString.isNullOrBlank()) return false
        val startDate = LocalDateTime.parse(freePremiumDateStartString, dateTimeFormatter)
        return startDate.plusDays(1) > LocalDateTime.now()
    }

    companion object {
        private const val FREE_PREMIUM_PERIOD_START = "com.maxpoliakov.skillapp.FREE_PREMIUM_PERIOD_START"
    }
}
