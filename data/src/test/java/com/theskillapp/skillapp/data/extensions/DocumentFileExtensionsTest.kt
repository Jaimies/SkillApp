package com.theskillapp.skillapp.data.extensions

import com.theskillapp.skillapp.shared.util.dateTimeFormatter
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class DocumentFileExtensionsTest : FunSpec({
    context("localDateTimeOfEpochMilli()") {
        withData<LocalDateTime>(
            { date -> date.format(dateTimeFormatter) },
            LocalDate.ofEpochDay(0).atStartOfDay(),
            LocalDate.ofEpochDay(1).atStartOfDay(),
            LocalDate.of(2021, 5, 15).atTime(10, 15)
        ) { date ->
            localDateTimeOfEpochMilli(date.atZone(ZoneId.systemDefault()).toEpochSecond() * 1_000) shouldBe date
        }
    }
})
