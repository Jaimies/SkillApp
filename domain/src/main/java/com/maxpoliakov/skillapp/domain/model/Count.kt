package com.maxpoliakov.skillapp.domain.model

data class Count(
    val times: Long,
) : Comparable<Count> {

    fun toTimes() = times

    override fun compareTo(other: Count): Int {
        return this.times.compareTo(other.times)
    }

    companion object {
        val ZERO = Count(0L)

        fun ofTimes(times: Long): Count = Count(times)
        fun ofTimes(times: Int): Count = ofTimes(times.toLong())
    }
}
