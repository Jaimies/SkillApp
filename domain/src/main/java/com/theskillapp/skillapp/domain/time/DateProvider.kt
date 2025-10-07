package com.theskillapp.skillapp.domain.time

import java.time.LocalDate

interface DateProvider {
    fun getCurrentDateWithRespectToDayStartTime(): LocalDate
}
