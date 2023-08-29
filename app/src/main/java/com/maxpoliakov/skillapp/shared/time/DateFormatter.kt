package com.maxpoliakov.skillapp.shared.time

import java.time.LocalDate

interface DateFormatter {
    fun format(date: LocalDate?): String
    fun shortFormat(date: LocalDate?): String
}
