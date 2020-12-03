package com.maxpoliakov.skillapp

import java.time.LocalDate

fun daysAgoDate(daysAgo: Long): LocalDate = LocalDate.now().minusDays(daysAgo)
