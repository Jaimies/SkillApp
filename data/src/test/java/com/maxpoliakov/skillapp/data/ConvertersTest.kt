package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.data.db.Converters
import com.maxpoliakov.skillapp.domain.model.Goal
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class ConvertersTest : StringSpec({
    val epoch = LocalDate.ofEpochDay(0)

    "fromLocalDate()" {
        Converters.fromLocalDate(epoch) shouldBe "1970-01-01"
    }

    "toLocalDate()" {
        Converters.toLocalDate("1970-01-01") shouldBe epoch
    }

    "toGoalType() and fromGoalType()" {
        listOf(Goal.Type.Daily, Goal.Type.Weekly).forEach { interval ->
            Converters.toGoalType(Converters.fromGoalType(interval)) shouldBe interval
        }
    }
})
