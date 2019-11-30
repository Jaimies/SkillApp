package com.jdevs.timeo.api.repository.firestore

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.USERS_COLLECTION

abstract class FirestoreBaseRepository(private val onLastItemCallback: () -> Unit = {}) {

    protected val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    protected val activitiesRef by lazy {
        firestore.collection("/$USERS_COLLECTION/${auth.currentUser?.uid}/${ActivitiesConstants.COLLECTION}")
    }

    protected val recordsRef by lazy {
        firestore.collection("/$USERS_COLLECTION/${auth.currentUser?.uid}/${RecordsConstants.COLLECTION}")
    }

    protected abstract val initialQuery: Query
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

    fun onFragmentDestroyed() {

        query = initialQuery
        isLastItemReached = false
        lastVisibleItem = null
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
