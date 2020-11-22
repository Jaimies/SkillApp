package com.jdevs.timeo.model

import java.time.Duration

interface Recordable : ViewItem {
    val name: String
    val totalTime: Duration
}
