package com.jdevs.timeo.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jdevs.timeo.R
import com.jdevs.timeo.data.ActivityOperation
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.utilities.ACTIVITIES_FETCH_LIMIT
import com.jdevs.timeo.utilities.TAG

class ActivitiesListLiveData(
    private val query: Query,
    private val onLastVisibleActivityCallback: OnLastVisibleActivityCallback,

    private val onLastActivityReachedCallback: OnLastActivityReachedCallback
) : LiveData<ActivityOperation>(),
    EventListener<QuerySnapshot> {

    private var listenerRegistration: ListenerRegistration? = null
    private var wasLoaderHidden = false

    override fun onActive() {
        listenerRegistration = query.addSnapshotListener(this)
        wasLoaderHidden = false
    }

    override fun onInactive() {
        listenerRegistration?.remove()
    }

    override fun onEvent(
        querySnapshot: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {

        if (exception != null || querySnapshot == null) {

            Log.w(TAG, "Failed to get data from Firestore", exception)

            return
        }

        if (!wasLoaderHidden) {

            value = ActivityOperation(null, R.id.OPERATION_LOADED, "")
            wasLoaderHidden = true
        }

        for (documentChange in querySnapshot.documentChanges) {
            processDocumentChange(documentChange)
        }

        val querySnapshotSize = querySnapshot.size()

        if (querySnapshotSize < ACTIVITIES_FETCH_LIMIT) {

            onLastActivityReachedCallback.setLastActivityReached(true)
        } else {

            val lastVisibleProduct = querySnapshot.documents[querySnapshotSize - 1]

            onLastVisibleActivityCallback.setLastVisibleActivity(lastVisibleProduct)
        }
    }

    private fun processDocumentChange(documentChange: DocumentChange) {
        val activity = try {

            documentChange.document.toObject(TimeoActivity::class.java)
        } catch (e: RuntimeException) {

            e.printStackTrace()
            TimeoActivity()
        }

        val documentId = documentChange.document.id

        val operation = when (documentChange.type) {

            DocumentChange.Type.ADDED -> ActivityOperation(
                activity,
                R.id.OPERATION_ADDED,
                documentId
            )

            DocumentChange.Type.MODIFIED -> ActivityOperation(
                activity,
                R.id.OPERATION_MODIFIED,
                documentId
            )

            DocumentChange.Type.REMOVED -> ActivityOperation(
                activity,
                R.id.OPERATION_REMOVED,
                documentId
            )
        }

        value = operation
    }

    interface OnLastVisibleActivityCallback {
        fun setLastVisibleActivity(lastVisibleActivity: DocumentSnapshot)
    }

    interface OnLastActivityReachedCallback {
        fun setLastActivityReached(isLastActivityReached: Boolean)
    }
}
