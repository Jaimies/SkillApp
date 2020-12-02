package com.maxpoliakov.skillapp.util.time

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.clockOfEpochDay
import com.maxpoliakov.skillapp.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.Clock
import java.time.Duration
import java.time.LocalDate

class ConvertersTest : StringSpec({
    "get friendly time" {
        getFriendlyTime(Duration.ofMinutes(89)) shouldBe "1h 29m"
        getFriendlyTime(Duration.ofMinutes(60)) shouldBe "1h"
        getFriendlyTime(Duration.ZERO) shouldBe ""
        getFriendlyTime(Duration.ofMinutes(1439)) shouldBe "23h 59m"
    }

    "to readable float" {
        1f.toReadableFloat() shouldBe "1"
        0f.toReadableFloat() shouldBe "0"
        0.5f.toReadableFloat() shouldBe "0.5"
    }

    "get friendly hours" {
        getFriendlyHours(Duration.ofMinutes(120)) shouldBe "2"
        getFriendlyHours(Duration.ZERO) shouldBe "0"
        getFriendlyHours(Duration.ofMinutes(102)) shouldBe "1.7"
        getFriendlyHours(Duration.ofMinutes(101)) shouldBe "1.7"
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
