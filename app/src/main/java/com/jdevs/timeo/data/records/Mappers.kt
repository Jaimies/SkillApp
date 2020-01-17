package com.jdevs.timeo.data.records

import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.util.time.toDate
import com.jdevs.timeo.util.time.toOffsetDate
import javax.inject.Inject

class FirestoreRecordMapper @Inject constructor() : Mapper<Record, FirestoreRecord> {

    override fun map(input: Record) = input.run {
        FirestoreRecord(documentId, name, time, activityId, creationDate.toDate())
    }
}

class FirestoreDomainRecordMapper @Inject constructor() : Mapper<FirestoreRecord, Record> {

    override fun map(input: FirestoreRecord) = input.run {
        Record(
            documentId = documentId,
            name = name,
            time = time,
            activityId = activityId,
            creationDate = timestamp.toOffsetDate()
        )
    }
}

class DBRecordMapper @Inject constructor() : Mapper<Record, DBRecord> {

    override fun map(input: Record) = input.run {
        DBRecord(id, name, time, roomActivityId, creationDate)
    }
}

class DBDomainRecordMapper @Inject constructor() : Mapper<DBRecord, Record> {

    override fun map(input: DBRecord) = input.run {
        Record(
            id, name = name, time = time, roomActivityId = activityId, creationDate = creationDate
        )
    }
}
