package com.jdevs.timeo.domain.model

import com.jdevs.timeo.data.records.DBRecord
import com.jdevs.timeo.data.records.FirestoreRecord
import com.jdevs.timeo.util.ViewTypes.RECORD
import com.jdevs.timeo.util.time.toDate
import org.threeten.bp.OffsetDateTime

data class Record(
    override val id: Int = 0,
    override val documentId: String = "",
    val name: String,
    val time: Long,
    val activityId: String = "",
    val roomActivityId: Int = 0,
    override val creationDate: OffsetDateTime = OffsetDateTime.now()
) : Entity<DBRecord, FirestoreRecord>, DataItem {

    override val viewType = RECORD

    override fun toDB() = DBRecord(id, name, time, roomActivityId, creationDate)
    override fun toFirestore() =
        FirestoreRecord(documentId, name, time, activityId, creationDate.toDate())
}
