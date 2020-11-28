package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.data.db.Converters
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

class ConvertersTest : StringSpec({
    val converters = Converters()
    val epochDateTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)

    "fromLocalDateTime()" {
        converters.fromLocalDateTime(epochDateTime) shouldBe "1970-01-01T00:00:00"
    }

    "toLocalDateTime()" {
        converters.toLocalDateTime("1970-01-01T00:00:00") shouldBe epochDateTime
    }

    "toDuration()" {
        converters.toDuration(120) shouldBe Duration.ofHours(2)
        converters.toDuration(20) shouldBe Duration.ofMinutes(20)
    }

    "fromDuration()" {
        converters.fromDuration(Duration.ofHours(2)) shouldBe 120
        converters.fromDuration(Duration.ofMinutes(50)) shouldBe 50
    }
})
