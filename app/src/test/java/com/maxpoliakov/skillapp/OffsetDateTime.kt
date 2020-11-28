package com.maxpoliakov.skillapp

import java.time.LocalDateTime

fun daysAgoDate(daysAgo: Long): LocalDateTime = LocalDateTime.now().minusDays(daysAgo)
