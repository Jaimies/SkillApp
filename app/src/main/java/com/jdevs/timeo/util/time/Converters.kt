package com.jdevs.timeo.util.time

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import java.util.Date

fun Date?.toOffsetDate(): OffsetDateTime {

    return if (this == null) {

        OffsetDateTime.now()
    } else {

        OffsetDateTime.from(DateTimeUtils.toInstant(this).atOffset(ZoneOffset.UTC))
    }
}

fun OffsetDateTime.toDate(): Date = DateTimeUtils.toDate(toInstant())
