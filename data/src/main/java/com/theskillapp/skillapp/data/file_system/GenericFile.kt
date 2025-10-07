package com.theskillapp.skillapp.data.file_system

import com.theskillapp.skillapp.domain.model.GenericUri
import java.time.LocalDateTime

data class GenericFile(
    val uri: GenericUri,
    val lastModificationDate: LocalDateTime,
)
