package com.jdevs.timeo.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.util.AdapterConstants.STATISTIC

@Entity(tableName = "stats")
data class RecordStats(
    var time: Long = 0,
    @PrimaryKey var day: Long = 0
) : ViewItem {

    @Ignore
    override val viewType = STATISTIC

    @get:Ignore
    override val id
        get() = day.toInt()

    @Ignore
    override val documentId = ""
}
