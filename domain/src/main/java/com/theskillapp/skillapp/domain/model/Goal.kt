package com.theskillapp.skillapp.domain.model

data class Goal(val count: Long, val type: Type) {
    enum class Type(val interval: StatisticInterval) {
        Daily(StatisticInterval.Daily),
        Weekly(StatisticInterval.Weekly),
        Monthly(StatisticInterval.Monthly),
        Yearly(StatisticInterval.Yearly),
        Lifetime(StatisticInterval.Lifetime),
    }
}
