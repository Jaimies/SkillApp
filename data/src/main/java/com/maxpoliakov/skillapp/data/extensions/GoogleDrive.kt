package com.maxpoliakov.skillapp.data.extensions

import com.google.api.client.http.ByteArrayContent
import com.maxpoliakov.skillapp.domain.model.BackupData

fun BackupData.toByteArrayContent(mimeType: String = "text/plain"): ByteArrayContent {
    return ByteArrayContent.fromString(mimeType, contents)
}
