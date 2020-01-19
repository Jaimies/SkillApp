package com.jdevs.timeo.util.time

import com.jdevs.timeo.daysAgoDate
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.threeten.bp.OffsetDateTime

/**
 * Unit tests for Time utilities
 */
class TimeTest {

    /**
     * Test for [Long.toFriendlyTime]
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

            assertThat(it.first.toFriendlyTime(), `is`(it.second))
        }
    }

    /**
     * Test for [Long.getAvgWeekHours]
     * The test data is written in the following syntax: <totalMins> to <daysAgoStarted> to <expectedOutput>
     */
    @Test
    fun getAvgWeekHours_isCorrect() {

        val dataset = listOf(
            238L to 7L to "2",
            444L to 8L to "3.7"
        )

        dataset.forEach {

            val mins = it.first.first
            val expectedOutput = it.second
            val date = daysAgoDate(it.first.second)

            assertThat(mins.getAvgWeekHours(date), `is`(expectedOutput))
        }
    }

    /**
     * Test for [OffsetDateTime.getDaysSpentSince]
     * The test data is written in the following syntax: <daysAgoStarted>
     */
    @Test
    fun getDaysSpentSince_isCorrect() {

        val dataset = listOf(1L, 7L, 8L, 3L, 0L)

        dataset.forEach {

            val date = daysAgoDate(it)

            assertThat(date.getDaysSpentSince(), `is`(it + 1))
        }
    }

    /**
     * Test for [Long.toHours]
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

            assertThat(it.first.toHours(), `is`(it.second))
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

            assertThat(it.first.toMins(), `is`(it.second))
        }
    }
}
