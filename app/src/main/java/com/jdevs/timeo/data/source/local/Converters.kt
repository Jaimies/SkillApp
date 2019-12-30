package com.jdevs.timeo.data.source.local

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long) = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time
}
