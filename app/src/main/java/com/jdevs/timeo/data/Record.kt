package com.jdevs.timeo.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.util.AdapterConstants
import java.util.Calendar
import java.util.Date

@Keep
@Entity(tableName = "records")
data class Record(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "",
    var time: Long = 0,
    var activityId: String = "",
    @Ignore
    @ServerTimestamp var timestamp: Date = Calendar.getInstance().time
) : ViewType {

    @Exclude
    override fun getViewType() = AdapterConstants.RECORD
}
