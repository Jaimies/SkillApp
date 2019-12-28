package com.jdevs.timeo.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.Date

/**
 * Unit tests for Time utilities
 */
class TimeOperationsTest {

    /**
     * Test for [Long.getFriendlyTime]
     * The test data is written in the following syntax: <input> to <expectedOutput>
     */
    @Test
    fun getFriendlyTime_isCorrect() {

        val dataset = listOf(
            89L to "1h 29m",
            60L to "1h",
            0L to "",
            1439L to "23h 59m"
        )

        dataset.forEach {

            assertThat(it.first.getFriendlyTime(), `is`(it.second))
        }
    }

    /**
     * Test for [Long.getAvgDailyHours]
     * The test data is written in the following syntax: <totalMins> to <daysAgoStarted> to <expectedOutput>
     */
    @Test
    fun getAvgDailyHours_isCorrect() {

        val dataset = listOf(
            238L to 7 to "0.5",
            444L to 8 to "0.8"
        )

        dataset.forEach {

            val mins = it.first.first
            val expectedOutput = it.second
            val date = daysAgoDate(it.first.second)

            assertThat(mins.getAvgDailyHours(date), `is`(expectedOutput))
        }
    }

    /**
     * Test for [Date.getDaysSpentSince]
     * The test data is written in the following syntax: <daysAgoStarted>
     */
    @Test
    fun getDaysSpentSince_isCorrect() {

        val dataset = listOf(7, 8, 3, 0)

        dataset.forEach {

            val date = daysAgoDate(it)

            assertThat(date.getDaysSpentSince(), `is`(it + 1))
        }
    }

    /**
     * Test for [Long.getHours]
     * The test data is written in the following syntax: <input> to <expectedOutput>
     */
    @Test
    fun getHours_isCorrect() {

        val dataset = listOf(
            90L to "1.5",
            0L to "0",
            102L to "1.7",
            101L to "1.7"
        )

        dataset.forEach {

            assertThat(it.first.getHours(), `is`(it.second))
        }
    }

    /**
     * Test for [Pair<Long, Long>.getMins]
     * The test data is written in the following syntax: <mins> to <hours> to <expectedOutput>
     */
    @Test
    fun getMins_isCorrect() {

        val dataset = listOf(
            10L to 30L to 630L,
            3L to 49L to 229L,
            18L to 17L to 1097L
        )

        dataset.forEach {

            assertThat(it.first.getMins(), `is`(it.second))
        }
    }
}
