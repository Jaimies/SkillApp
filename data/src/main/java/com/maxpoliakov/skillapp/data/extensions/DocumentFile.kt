package com.maxpoliakov.skillapp.data.extensions

import androidx.documentfile.provider.DocumentFile
import com.maxpoliakov.skillapp.data.file_system.GenericFile
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

val DocumentFile.lastModifiedDate
    get() = localDateTimeOfEpochMilli(lastModified())

private fun localDateTimeOfEpochMilli(epochMilli: Long): LocalDateTime {
    return Instant.ofEpochMilli(epochMilli)
        .atZone(ZoneOffset.UTC)
        .toLocalDateTime()
}

fun DocumentFile.toGenericFile() = GenericFile(
    uri.toGenericUri(),
    name.orEmpty(),
    lastModifiedDate,
)
