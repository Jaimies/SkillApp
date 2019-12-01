package com.jdevs.timeo.api.livedata

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
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.data.operations.Operation
import com.jdevs.timeo.util.ActivitiesConstants.FETCH_LIMIT
import com.jdevs.timeo.util.TAG

abstract class ItemListLiveData(
    private val query: Query,
    private val setLastVisibleActivity: (DocumentSnapshot) -> Unit,
    private val onLastActivityReached: () -> Unit
) : LiveData<Operation>(),
    EventListener<QuerySnapshot> {

    private var listenerRegistration: ListenerRegistration? = null
    private var wasLoaderHidden = false

    protected abstract val dataType: Class<*>
    protected abstract val operationConstructor: (Any?, Int, String) -> Operation

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

            value = operationConstructor(null, R.id.OPERATION_LOADED, "")
            wasLoaderHidden = true
        }

        for (documentChange in querySnapshot.documentChanges) {
            processDocumentChange(documentChange)
        }

        value = operationConstructor(null, R.id.OPERATION_FINISHED, "")

        val querySnapshotSize = querySnapshot.size()

        if (querySnapshotSize < FETCH_LIMIT) {

            onLastActivityReached()
        } else {

            val lastVisibleProduct = querySnapshot.documents[querySnapshotSize - 1]

            setLastVisibleActivity(lastVisibleProduct)
        }
    }

    private fun processDocumentChange(documentChange: DocumentChange) {
        val activity = try {

            documentChange.document.toObject(dataType)
        } catch (e: RuntimeException) {

            e.printStackTrace()
            TimeoActivity()
        }

        val documentId = documentChange.document.id

        val operation = when (documentChange.type) {

            DocumentChange.Type.ADDED ->
                operationConstructor(activity, R.id.OPERATION_ADDED, documentId)

            DocumentChange.Type.MODIFIED ->
                operationConstructor(activity, R.id.OPERATION_MODIFIED, documentId)

            DocumentChange.Type.REMOVED ->
                operationConstructor(activity, R.id.OPERATION_REMOVED, documentId)
        }

        value = operation
    }
}
