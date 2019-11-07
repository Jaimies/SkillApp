package com.jdevs.timeo.helpers

import org.joda.time.DateTime
import org.joda.time.Days
import java.util.*

class TimeHelper {

    companion object {

        private const val HOUR_MINUTES = 60

        fun minsToTime(mins: Int): Pair<Int, Int> {

            val hours = mins / HOUR_MINUTES
            val minutes = mins % HOUR_MINUTES

            return Pair(hours, minutes)
        }

        fun minsToHours(mins: Int): String {

            val time = mins / HOUR_MINUTES.toFloat()

            val timeString = "%.1f".format(time)

            return if (timeString.takeLast(1) == "0") timeString.dropLast(2) else timeString
        }

        fun timeToMins(time: Pair<Int, Int>): Int {

            return time.first * HOUR_MINUTES + time.second
        }

        fun getHoursSinceDate(date: Date): Int {

            val currentTime = DateTime()

            val creationTime = DateTime(date)

            val daysDiff = Days.daysBetween(creationTime, currentTime)

            return daysDiff.days + 1
        }
    }
}
