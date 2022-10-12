package com.maxpoliakov.skillapp.domain.model

data class Goal(val count: Long, val type: Type) {
    enum class Type(val interval: StatisticInterval) {
        Daily(StatisticInterval.Daily),
        Weekly(StatisticInterval.Weekly),
    }
}
