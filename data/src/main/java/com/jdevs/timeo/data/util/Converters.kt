package com.jdevs.timeo.data.util

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.OffsetDateTime
import java.util.Date

fun Date?.toOffsetDate(): OffsetDateTime {

    if (this == null) {

        return OffsetDateTime.now()
    }

    return OffsetDateTime.from(DateTimeUtils.toInstant(this).atOffset(currentOffset))
}

fun OffsetDateTime.toDate(): Date = DateTimeUtils.toDate(toInstant())
