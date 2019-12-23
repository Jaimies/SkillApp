package com.jdevs.timeo.data

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.util.AdapterConstants.ACTIVITY
import kotlinx.android.parcel.Parcelize
import java.util.Calendar
import java.util.Date

@Keep
@Parcelize
@Entity(tableName = "activities")
data class Activity(
    @Exclude
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,

    var name: String = "",
    var totalTime: Long = 0,

    @Ignore
    var timestamp: Date = Calendar.getInstance().time
) : Parcelable, ViewType {

    override fun getViewType() = ACTIVITY
}
