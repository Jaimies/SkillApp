package com.jdevs.timeo.helpers

class TimeHelper {

    companion object {

        fun minsToTime(mins: Int): Pair<Int, Int> {

            val hours = mins / 60
            val minutes = mins % 60

            return Pair(hours, minutes)

        }

        fun minsToHours(mins: Int): String {

            val time = mins / 60.0f

            val timeString = "%.1f".format(time)


            return if (timeString.takeLast(1) == "0") timeString.dropLast(2) else timeString

        }


        fun timeToMins(time: Pair<Int, Int>): Int {

            return time.first * 60 + time.second

        }

    }
}