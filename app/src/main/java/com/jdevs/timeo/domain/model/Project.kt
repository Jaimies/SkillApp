package com.jdevs.timeo.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.jdevs.timeo.util.ViewTypes.PROJECT
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Keep
@Parcelize
data class Project(
    override val id: Int = 0,
    override val documentId: String = "",
    val name: String,
    val totalTime: Long = 0,
    val lastWeekTime: Int = 0,
    override val creationDate: OffsetDateTime = OffsetDateTime.now()
) : DataItem, Parcelable {

    @IgnoredOnParcel
    override val viewType = PROJECT
}
