package com.jdevs.timeo.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.livedata.ActivityListLiveData
import com.jdevs.timeo.repository.common.BaseFirestoreRepository
import com.jdevs.timeo.util.ACTIVITIES_ACTIVITY_ID_PROPERTY
import com.jdevs.timeo.util.ACTIVITIES_FETCH_LIMIT
import com.jdevs.timeo.util.ACTIVITIES_NAME_PROPERTY
import com.jdevs.timeo.util.ACTIVITIES_TIMESTAMP_PROPERTY
import com.jdevs.timeo.util.ACTIVITIES_TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.logOnFailure
import com.jdevs.timeo.viewmodel.ActivityListViewModel

class FirestoreActivityListRepository(lastItemCallback: () -> Unit = {}) :
    BaseFirestoreRepository(lastItemCallback),
    ActivityListViewModel.Repository {

    override var query = activitiesRef
        .orderBy(ACTIVITIES_TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
        .limit(ACTIVITIES_FETCH_LIMIT)

    override val liveDataConstructor = ::ActivityListLiveData

    override fun createRecord(activityName: String, time: Long, activityId: String) {

        val record = Record(activityName, time, activityId)

        recordsRef.add(record)

        activitiesRef
            .document(activityId)
            .update(ACTIVITIES_TOTAL_TIME_PROPERTY, FieldValue.increment(time))
            .logOnFailure("Failed to update data in Firestore")
    }

    override fun updateActivity(activity: TimeoActivity, activityId: String) {

        val activityReference = activitiesRef.document(activityId)

        recordsRef.whereEqualTo(ACTIVITIES_ACTIVITY_ID_PROPERTY, activityId).get()
            .addOnSuccessListener { querySnapshot ->

                val recordReferences = ArrayList<DocumentReference>()

                for (record in querySnapshot.documents) {

                    recordReferences.add(record.reference)
                }

                firestore.runBatch { batch ->

                    batch.set(activityReference, activity)

                    for (recordReference in recordReferences) {

                        batch.update(recordReference, ACTIVITIES_NAME_PROPERTY, activity.name)
                    }
                }
                    .logOnFailure("Failed to save data to Firestore")
            }
    }

    override fun createActivity(activity: TimeoActivity) {

        activitiesRef.add(activity)
            .logOnFailure("Failed to add data to Firestore")
    }

    override fun deleteActivity(activityId: String) {

        activitiesRef.document(activityId).delete()
            .logOnFailure("Failed to delete data to Firestore")
    }
}
