package com.jdevs.timeo.domain.model

import com.jdevs.timeo.util.ViewTypes.RECORD
import org.threeten.bp.OffsetDateTime

data class Record(
    override val id: Int = 0,
    override val documentId: String = "",
    val name: String,
    val time: Long,
    val activityId: String = "",
    val roomActivityId: Int = 0,
    override val creationDate: OffsetDateTime = OffsetDateTime.now()
) : DataItem {

    override val viewType = RECORD
}
