package com.maxpoliakov.skillapp.domain.model

import java.time.Duration

data class Goal(val time: Duration, val type: Type) {
    enum class Type { Daily, Weekly }
}
