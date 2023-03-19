package com.maxpoliakov.skillapp.domain.model

data class Distance(
    val meters: Long,
) : Comparable<Distance> {
    fun toMeters() = meters
    fun toMetersPart() = meters % 1_000L
    fun toKilometers() = meters / 1_000L

    fun plusMeters(meters: Long) = Distance(this.meters + meters)

    override fun compareTo(other: Distance): Int {
        return this.meters.compareTo(other.meters)
    }

    companion object {
        val ZERO = Distance(0)

        fun ofMeters(meters: Long) = Distance(meters)
        fun ofKilometers(kilometers: Long) = Distance(kilometers * 1_000L)
        fun ofKilometers(kilometers: Int) = ofKilometers(kilometers.toLong())
    }
}
