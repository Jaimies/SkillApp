package com.jdevs.timeo.helpers

class TimeHelper {

    companion object {

        fun minsToTime(mins: Int): Pair<Int, Int> {

            val hours = mins / 60
            val minutes = mins % 60

            return Pair(hours, minutes)

        }

        fun minsToHours(mins: Int): String {

            return "%.1f".format(mins / 60f)

        }


        fun timeToMins(time: Pair<Int, Int>): Int {

            return time.first * 60 + time.second

        }

    }
}