package com.jdevs.timeo.data

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.jdevs.timeo.util.AdapterConstants.ACTIVITY
import com.jdevs.timeo.util.FirestoreConstants.TIMESTAMP_PROPERTY
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime
import java.util.Date

@Keep
@Parcelize
@Entity(tableName = "activities")
data class Activity(
    @get:Exclude
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,

    @Ignore
    @DocumentId
    override var documentId: String = "",

    var name: String = "",
    var totalTime: Long = 0,
    var lastWeekTime: Long = 0,

    @get:Exclude
    override var creationDate: OffsetDateTime = OffsetDateTime.now()
) : Parcelable, DataItem() {

    @Ignore
    @IgnoredOnParcel
    @get:PropertyName(TIMESTAMP_PROPERTY)
    @set:PropertyName(TIMESTAMP_PROPERTY)
    override var firestoreTimestamp: Date? = null

    @Ignore
    @get:Exclude
    @IgnoredOnParcel
    override val viewType = ACTIVITY
}
