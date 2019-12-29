package com.jdevs.timeo

import java.util.Date

fun daysAgoDate(daysAgo: Int): Date {

    return Date(Date().time - daysAgo * 24 * 60 * 60 * 1000)
}
