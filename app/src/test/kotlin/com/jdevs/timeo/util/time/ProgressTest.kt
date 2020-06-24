package com.jdevs.timeo.util.time

import com.jdevs.timeo.daysAgoDate
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ProgressTest {

    @Test
    fun getAvgWeekHours() {
        assertThat(getAvgWeekHours(238, daysAgoDate(7)), `is`("2"))
        assertThat(getAvgWeekHours(444, daysAgoDate(8)), `is`("3.7"))
    }

    @Test
    fun getDaysSpentSince() {
        assertThat(daysAgoDate(1).getDaysSpentSince(), `is`(2L))
        assertThat(daysAgoDate(7).getDaysSpentSince(), `is`(8L))
        assertThat(daysAgoDate(0).getDaysSpentSince(), `is`(1L))
        assertThat(daysAgoDate(-1).getDaysSpentSince(), `is`(1L))
    }
}
