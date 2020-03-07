package com.jdevs.timeo

import org.threeten.bp.OffsetDateTime

fun daysAgoDate(daysAgo: Long): OffsetDateTime = OffsetDateTime.now().minusDays(daysAgo)
