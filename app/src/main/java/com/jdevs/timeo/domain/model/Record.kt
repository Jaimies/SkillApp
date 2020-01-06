package com.jdevs.timeo.domain.model

import com.jdevs.timeo.data.db.model.DBRecord
import com.jdevs.timeo.data.firestore.model.FirestoreRecord
import com.jdevs.timeo.util.AdapterConstants.RECORD
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
) : DataItem() {

    override val viewType = RECORD

    fun toDBRecord() = DBRecord(id, name, time, roomActivityId, creationDate)
    fun toFirestoreRecord() =
        FirestoreRecord(documentId, name, time, activityId, creationDate.toDate())
}
