package com.maxpoliakov.skillapp.shared.time

import android.content.Context
import com.maxpoliakov.skillapp.R
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.LocalDate

class ConvertersTest : StringSpec({
    "to readable float" {
        1f.toReadableFloat() shouldBe "1"
        0f.toReadableFloat() shouldBe "0"
        0.5f.toReadableFloat() shouldBe "0.5"
    }

    "toReadableDate()" {
        val currentDate = LocalDate.ofEpochDay(9)
        val context = Mockito.mock(Context::class.java)

        `when`(context.getString(R.string.today)).thenReturn("Today")
        `when`(context.getString(R.string.yesterday)).thenReturn("Yesterday")
        `when`(context.getString(R.string.date, "Thu", "Jan", 8)).thenReturn("Thu, Jan 8")
        `when`(context.getString(R.string.date_with_year, "Wed", "Dec", 31, 1969)).thenReturn("Wed, Dec 31, 1969")

        context.toReadableDate(LocalDate.ofEpochDay(9), currentDate) shouldBe "Today"
        context.toReadableDate(LocalDate.ofEpochDay(8), currentDate) shouldBe "Yesterday"
        context.toReadableDate(LocalDate.ofEpochDay(7), currentDate) shouldBe "Thu, Jan 8"
        context.toReadableDate(LocalDate.ofEpochDay(-1), currentDate) shouldBe "Wed, Dec 31, 1969"
    }
})
