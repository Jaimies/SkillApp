package com.theskillapp.skillapp.domain.time

import com.theskillapp.skillapp.domain.repository.UserPreferenceRepository
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class DefaultDateProvider @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val clock: Clock,
) : DateProvider {
    override fun getCurrentDateWithRespectToDayStartTime(): LocalDate {
        if (LocalTime.now(clock) >= userPreferenceRepository.getDayStartTime()) {
            return LocalDate.now(clock)
        }

        return LocalDate.now(clock).minusDays(1)
    }
}
