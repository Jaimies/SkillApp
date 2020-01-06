package com.jdevs.timeo.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.util.AdapterConstants.STATISTIC

sealed class Stats : ViewItem {

    abstract val time: Long

    @Ignore
    @DocumentId
    override var documentId = ""

    @Ignore
    @get:Exclude
    override val viewType = STATISTIC

    abstract fun setupTime()
}

@Entity(tableName = "dayStats")
data class DayStats(
    override var time: Long = 0,
    @PrimaryKey var day: Long = 0
) : Stats() {

    @get:Ignore
    @get:Exclude
    override val id
        get() = day.toInt()

    override fun setupTime() {

        day = documentId.toLongOrNull() ?: return
    }
}

@Entity(tableName = "weekStats")
data class WeekStats(
    override var time: Long = 0,
    @PrimaryKey var week: Int = 0
) : Stats() {

    @get:Ignore
    @get:Exclude
    override val id
        get() = week

    override fun setupTime() {

        week = documentId.toIntOrNull() ?: return
    }
}

@Entity(tableName = "monthStats")
data class MonthStats(
    override var time: Long = 0,
    @PrimaryKey var month: Int = 0
) : Stats() {

    @get:Ignore
    @get:Exclude
    override val id
        get() = month

    override fun setupTime() {

        month = documentId.toIntOrNull() ?: return
    }
}
