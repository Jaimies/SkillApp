package com.jdevs.timeo.data

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.adapter.delegates.AdapterConstants
import com.jdevs.timeo.adapter.delegates.ViewType
import kotlinx.android.parcel.Parcelize
import java.util.Calendar
import java.util.Date

// TimeoActivity (task) class
@Keep
@Parcelize
data class TimeoActivity(
    var name: String = "",
    var icon: String = "",
    var totalTime: Long = 0,
    @ServerTimestamp var timestamp: Date = Calendar.getInstance().time
) : Parcelable, ViewType {

    override fun getViewType() = AdapterConstants.ACTIVITY
}
