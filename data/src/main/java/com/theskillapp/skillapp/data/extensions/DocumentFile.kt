package com.theskillapp.skillapp.data.extensions

import androidx.documentfile.provider.DocumentFile
import com.theskillapp.skillapp.data.file_system.GenericFile
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZoneId

val DocumentFile.lastModifiedDate
    get() = localDateTimeOfEpochMilli(lastModified())

fun localDateTimeOfEpochMilli(epochMilli: Long): LocalDateTime {
    return Instant.ofEpochMilli(epochMilli)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun DocumentFile.toGenericFile() = GenericFile(
    uri.toGenericUri(),
    name.orEmpty(),
    lastModifiedDate,
)
