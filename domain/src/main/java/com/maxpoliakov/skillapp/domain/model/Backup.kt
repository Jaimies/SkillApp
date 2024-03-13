package com.maxpoliakov.skillapp.domain.model

import java.time.LocalDateTime

data class Backup(
    val uri: GenericUri,
    val creationDate: LocalDateTime,
)
