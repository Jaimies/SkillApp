package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.data.db.Converters
import com.maxpoliakov.skillapp.domain.model.TimeTarget
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

    "toDuration() and fromDuration()" {
        listOf(Duration.ofMillis(3), Duration.ofMillis(5)).forEach { time ->
            converters.toDuration(converters.fromDuration(time)) shouldBe time
        }

        converters.fromDuration(null) shouldBe null
        converters.toDuration(null) shouldBe null
    }

    "toTimeTargetInterval() and fromTimeTargetInterval()" {
        converters.fromTimeTargetInterval(null) shouldBe null
        converters.toTimeTargetInterval(null) shouldBe null

        listOf(TimeTarget.Interval.Daily, TimeTarget.Interval.Weekly).forEach { interval ->
            converters.toTimeTargetInterval(converters.fromTimeTargetInterval(interval)) shouldBe interval
        }
    }
})
