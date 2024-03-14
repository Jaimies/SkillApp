package com.maxpoliakov.skillapp.data.extensions

import com.google.api.client.http.ByteArrayContent
import com.google.api.client.util.DateTime
import com.google.api.services.drive.model.File
import com.maxpoliakov.skillapp.data.file_system.GenericFile
import com.maxpoliakov.skillapp.domain.model.GenericUri
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun String.toByteArrayContent(mimeType: String = "text/plain"): ByteArrayContent {
    return ByteArrayContent.fromString(mimeType, this)
}

fun DateTime.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this.value),
        ZoneId.systemDefault(),
    )
}

fun File.toGenericFile() = GenericFile(
    GenericUri(this.id),
    this.name,
    this.modifiedTime.toLocalDateTime(),
)
