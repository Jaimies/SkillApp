package com.jdevs.timeo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stats")
data class RecordStats(
    var time: Long = 0,
    @PrimaryKey var day: Long = 0
)
