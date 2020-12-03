package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.data.db.Converters
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDate

class ConvertersTest : StringSpec({
    val converters = Converters()
    val epoch = LocalDate.ofEpochDay(0)

    "fromLocalDate()" {
        converters.fromLocalDate(epoch) shouldBe "1970-01-01"
    }

    "toLocalDate()" {
        converters.toLocalDate("1970-01-01") shouldBe epoch
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
