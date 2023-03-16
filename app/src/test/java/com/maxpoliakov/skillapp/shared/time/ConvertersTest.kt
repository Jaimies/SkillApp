package com.maxpoliakov.skillapp.shared.time

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.test.clockOfEpochDay
import com.maxpoliakov.skillapp.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.Clock
import java.time.Duration
import java.time.LocalDate

class ConvertersTest : StringSpec({
    "to readable float" {
        1f.toReadableFloat() shouldBe "1"
        0f.toReadableFloat() shouldBe "0"
        0.5f.toReadableFloat() shouldBe "0.5"
    }

    "get friendly hours" {
        Duration.ofMinutes(120).toReadableHours() shouldBe "2"
        Duration.ZERO.toReadableHours() shouldBe "0"
        Duration.ofMinutes(102).toReadableHours() shouldBe "1.7"
        Duration.ofMinutes(101).toReadableHours() shouldBe "1.7"
    }

    "toReadableDate()" {
        setClock(clockOfEpochDay(9))
        val context = Mockito.mock(Context::class.java)

        `when`(context.getString(R.string.today)).thenReturn("Today")
        `when`(context.getString(R.string.yesterday)).thenReturn("Yesterday")
        `when`(context.getString(R.string.date, "Thu", "Jan", 8)).thenReturn("Thu, Jan 8")
        `when`(context.getString(R.string.date_with_year, "Wed", "Dec", 31, 1969)).thenReturn("Wed, Dec 31, 1969")

        context.toReadableDate(LocalDate.ofEpochDay(9)) shouldBe "Today"
        context.toReadableDate(LocalDate.ofEpochDay(8)) shouldBe "Yesterday"
        context.toReadableDate(LocalDate.ofEpochDay(7)) shouldBe "Thu, Jan 8"
        context.toReadableDate(LocalDate.ofEpochDay(-1)) shouldBe "Wed, Dec 31, 1969"

        setClock(Clock.systemDefaultZone())
    }
})
