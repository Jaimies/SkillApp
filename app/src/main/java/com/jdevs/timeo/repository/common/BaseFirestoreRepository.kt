package com.jdevs.timeo.repository.common

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.util.ACTIVITIES_COLLECTION
import com.jdevs.timeo.util.RECORDS_COLLECTION
import com.jdevs.timeo.util.USERS_COLLECTION

abstract class BaseFirestoreRepository(private val onLastItemCallback: () -> Unit = {}) {

    protected val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    protected val activitiesRef by lazy {
        firestore.collection("/$USERS_COLLECTION/${auth.currentUser?.uid}/$ACTIVITIES_COLLECTION")
    }

    protected val recordsRef by lazy {
        firestore.collection("/$USERS_COLLECTION/${auth.currentUser?.uid}/$RECORDS_COLLECTION")
    }

    protected abstract var query: Query
    protected abstract val liveDataConstructor: (Query, (DocumentSnapshot) -> Unit, () -> Unit) -> LiveData<*>

    private var isLastItemReached = false
    private var lastVisibleItem: DocumentSnapshot? = null

    fun getLiveData(): LiveData<*>? {

        if (isLastItemReached) {

            return null
        }

        val lastItem = lastVisibleItem

        if (lastItem != null) {

            query = query.startAfter(lastItem)
        }

        return liveDataConstructor(query, ::setLastVisibleItem, ::onLastItemReached)
    }

    private fun onLastItemReached() {

        if (!isLastItemReached) {

            isLastItemReached = true
            onLastItemCallback()
        }
    }

    private fun setLastVisibleItem(lastVisibleItem: DocumentSnapshot) {

        this.lastVisibleItem = lastVisibleItem
    }
}
