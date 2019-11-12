package com.jdevs.timeo.utilities

import org.joda.time.DateTime
import org.joda.time.Days
import java.util.Date

class TimeUtility {

    companion object {

        private const val HOUR_MINUTES = 60

        fun minsToTime(mins: Int): String {

            val hours = mins / HOUR_MINUTES
            val minutes = mins % HOUR_MINUTES

            var timeString = ""

            if (hours != 0) {

                timeString += "${hours}h"
            }

            if (minutes != 0) {

                if (hours != 0) {

                    timeString += " "
                }

                timeString += "${minutes}m"
            }

            return timeString
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

            return if (daysDiff.days > 0) daysDiff.days + 1 else 1
        }
    }
}
