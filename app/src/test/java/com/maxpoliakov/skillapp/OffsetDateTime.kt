package com.maxpoliakov.skillapp

import java.time.OffsetDateTime

fun daysAgoDate(daysAgo: Long): OffsetDateTime = OffsetDateTime.now().minusDays(daysAgo)
