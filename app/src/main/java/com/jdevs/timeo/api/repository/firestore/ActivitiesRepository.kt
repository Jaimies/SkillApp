package com.jdevs.timeo.api.repository.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.jdevs.timeo.api.livedata.ActivityListLiveData
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.ui.activities.viewmodel.ActivityListViewModel
import com.jdevs.timeo.util.ActivitiesConstants.ACTIVITY_ID_PROPERTY
import com.jdevs.timeo.util.ActivitiesConstants.FETCH_LIMIT
import com.jdevs.timeo.util.ActivitiesConstants.NAME_PROPERTY
import com.jdevs.timeo.util.ActivitiesConstants.TIMESTAMP_PROPERTY
import com.jdevs.timeo.util.ActivitiesConstants.TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.logOnFailure

object ActivitiesRepository : ItemListRepository(),
    ActivityListViewModel.Repository {

    override val liveData = ::ActivityListLiveData

    operator fun invoke(lastItemCallback: () -> Unit = {}): ActivitiesRepository {

        super.reset(lastItemCallback)
        return this
    }

    override fun createRecord(activityName: String, time: Long, activityId: String) {

        val record = Record(activityName, time, activityId)

        recordsRef.add(record)

        activitiesRef
            .document(activityId)
            .update(TOTAL_TIME_PROPERTY, FieldValue.increment(time))
            .logOnFailure("Failed to update data in Firestore")
    }

    override fun updateActivity(activity: TimeoActivity, activityId: String) {

        val activityReference = activitiesRef.document(activityId)

        recordsRef.whereEqualTo(ACTIVITY_ID_PROPERTY, activityId).get()
            .addOnSuccessListener { querySnapshot ->

                val recordReferences = mutableListOf<DocumentReference>()

                for (record in querySnapshot.documents) {

                    recordReferences.add(record.reference)
                }

                firestore.runBatch { batch ->

                    batch.set(activityReference, activity)

                    for (recordReference in recordReferences) {

                        batch.update(recordReference, NAME_PROPERTY, activity.name)
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

    override fun createQuery(): Query {
        return activitiesRef
            .orderBy(TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
            .limit(FETCH_LIMIT)
    }
}
