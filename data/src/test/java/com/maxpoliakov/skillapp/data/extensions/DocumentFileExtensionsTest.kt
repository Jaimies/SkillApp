package com.maxpoliakov.skillapp.data.extensions

import androidx.documentfile.provider.DocumentFile
import com.maxpoliakov.skillapp.shared.util.dateTimeFormatter
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class DocumentFileExtensionsTest : FunSpec({
    context("lastModifiedDate") {
        val documentFile = mockk<DocumentFile>()

        withData<LocalDateTime>(
            { date -> date.format(dateTimeFormatter) },
            LocalDate.ofEpochDay(0).atStartOfDay(),
            LocalDate.ofEpochDay(1).atStartOfDay(),
            LocalDate.of(2021, 5, 15).atTime(10, 15)
        ) { date ->
            every { documentFile.lastModified() } returns date.toEpochSecond(ZoneOffset.UTC) * 1_000
            documentFile.lastModifiedDate shouldBe date
        }
    }
})
