package com.maxpoliakov.skillapp.model

import java.time.Duration

interface Recordable : ViewItem {
    val name: String
    val totalTime: Duration
}