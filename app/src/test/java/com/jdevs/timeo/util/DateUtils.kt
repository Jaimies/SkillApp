package com.jdevs.timeo.util

import org.joda.time.DateTime
import java.util.Date

fun daysAgoDate(daysAgo: Int): Date {

    return DateTime(Date().time - daysAgo * 24 * 60 * 60 * 1000).toDate()
}
