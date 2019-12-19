package com.jdevs.timeo.data

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.util.AdapterConstants
import kotlinx.android.parcel.Parcelize
import java.util.Calendar
import java.util.Date

@Keep
@Parcelize
data class Task(
    var name: String = "",
    var totalTime: Long = 0,
    @ServerTimestamp var timestamp: Date = Calendar.getInstance().time
) : Parcelable, ViewType {

    @Exclude
    override fun getViewType() = AdapterConstants.ACTIVITY
}
