package com.maxpoliakov.skillapp.shared.time

import android.content.Context
import com.maxpoliakov.skillapp.domain.time.StubDateProvider
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.LocalDate
import com.maxpoliakov.skillapp.R

class DefaultDateFormatterTest : StringSpec({
    "format()" {
        val dateProvider = StubDateProvider(LocalDate.ofEpochDay(9))

        val context = Mockito.mock(Context::class.java)

        `when`(context.getString(R.string.today)).thenReturn("Today")
        `when`(context.getString(R.string.yesterday)).thenReturn("Yesterday")
        `when`(context.getString(R.string.date, "Thu", "Jan", 8)).thenReturn("Thu, Jan 8")
        `when`(context.getString(R.string.date_with_year, "Wed", "Dec", 31, 1969)).thenReturn("Wed, Dec 31, 1969")

        val formatter = DefaultDateFormatter(context, dateProvider)

        formatter.format(LocalDate.ofEpochDay(9)) shouldBe "Today"
        formatter.format(LocalDate.ofEpochDay(8)) shouldBe "Yesterday"
        formatter.format(LocalDate.ofEpochDay(7)) shouldBe "Thu, Jan 8"
        formatter.format(LocalDate.ofEpochDay(-1)) shouldBe "Wed, Dec 31, 1969"
    }
})
