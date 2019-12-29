package com.jdevs.timeo.data

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.util.AdapterConstants.ACTIVITY
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Keep
@Parcelize
@Entity(tableName = "activities")
data class Activity(
    @get:Exclude
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,

    @DocumentId
    override var documentId: String = "",

    var name: String = "",
    var totalTime: Long = 0,

    @Ignore
    @ServerTimestamp
    var timestamp: Date? = null
) : Parcelable, DataItem {

    @Exclude
    @Ignore
    override fun getViewType() = ACTIVITY
}
