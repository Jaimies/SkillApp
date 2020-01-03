package com.jdevs.timeo.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.util.AdapterConstants.STATISTIC

sealed class Stats {

    abstract val time: Long
}

@Entity(tableName = "dayStats")
data class DayStats(
    override var time: Long = 0,
    @get:Exclude
    @PrimaryKey var day: Long = 0
) : Stats(), ViewItem {

    @Ignore
    @get:Exclude
    override val viewType = STATISTIC

    @get:Ignore
    @get:Exclude
    override val id
        get() = day.toInt()

    @Ignore
    @DocumentId
    override var documentId = ""

    fun setupDaysCount() {

        day = documentId.toLongOrNull() ?: return
    }
}

@Entity(tableName = "weekStats")
data class WeekStats(
    override var time: Long = 0,
    @PrimaryKey
    var week: Short = 0
) : Stats(), ViewItem {

    @Ignore
    override val viewType = STATISTIC

    @get:Ignore
    @get:Exclude
    override val id
        get() = week.toInt()

    @Ignore
    override val documentId = ""
}

@Entity(tableName = "monthStats")
data class MonthStats(
    override var time: Long = 0,
    @PrimaryKey
    var month: Short = 0
) : Stats(), ViewItem {

    @Ignore
    override val viewType = STATISTIC

    @get:Ignore
    @get:Exclude
    override val id
        get() = month.toInt()

    @Ignore
    override val documentId = ""
}
