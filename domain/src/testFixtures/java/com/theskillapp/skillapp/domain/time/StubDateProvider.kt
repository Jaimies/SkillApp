package com.theskillapp.skillapp.domain.time

import java.time.LocalDate

class StubDateProvider(private val date: LocalDate = LocalDate.EPOCH) : DateProvider {
    override fun getCurrentDateWithRespectToDayStartTime() = date
}
