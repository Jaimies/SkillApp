package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.time.DateProvider
import java.time.LocalDate

class StubDateProvider(private val date: LocalDate = LocalDate.EPOCH) : DateProvider {
    override fun getCurrentDateWithRespectToDayStartTime() = date
}
