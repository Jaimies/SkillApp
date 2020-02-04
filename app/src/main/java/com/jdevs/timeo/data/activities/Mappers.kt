package com.jdevs.timeo.data.activities

import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.util.time.toDate
import com.jdevs.timeo.util.time.toOffsetDate
import javax.inject.Inject

class FirestoreActivityMapper @Inject constructor() : Mapper<Activity, FirestoreActivity> {

    override fun map(input: Activity) = input.run {
        FirestoreActivity(documentId, name, totalTime, timestamp = creationDate.toDate())
    }
}

class FirestoreDomainActivityMapper @Inject constructor() : Mapper<FirestoreActivity, Activity> {

    override fun map(input: FirestoreActivity) = input.run {
        Activity(
            documentId = documentId,
            name = name,
            totalTime = totalTime,
            lastWeekTime = getLastWeekTime(),
            creationDate = timestamp.toOffsetDate()
        )
    }
}

class DBActivityMapper @Inject constructor() : Mapper<Activity, DBActivity> {

    override fun map(input: Activity) = input.run {
        DBActivity(id, name, totalTime, lastWeekTime, creationDate)
    }
}

class DBDomainActivityMapper @Inject constructor() : Mapper<DBActivity, Activity> {

    override fun map(input: DBActivity) = input.run {
        Activity(
            id,
            name = name,
            totalTime = totalTime,
            lastWeekTime = lastWeekTime,
            creationDate = creationDate
        )
    }
}
