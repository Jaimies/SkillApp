package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.data.db.Converters
import com.maxpoliakov.skillapp.domain.model.Goal
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
    }

    "toGoalType() and fromGoalType()" {
        listOf(Goal.Type.Daily, Goal.Type.Weekly).forEach { interval ->
            converters.toGoalType(converters.fromGoalType(interval)) shouldBe interval
        }
    }
})
