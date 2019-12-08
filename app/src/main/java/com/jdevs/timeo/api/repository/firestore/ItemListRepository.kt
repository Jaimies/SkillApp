package com.jdevs.timeo.api.repository.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.api.livedata.ItemListLiveData
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.UserConstants.USERS_COLLECTION

abstract class ItemListRepository : FirebaseAuth.AuthStateListener {

    protected lateinit var activitiesRef: CollectionReference
    protected lateinit var recordsRef: CollectionReference
    protected abstract val liveData: (Query?, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemListLiveData
    protected val firestore by lazy { FirebaseFirestore.getInstance() }

    private lateinit var query: Query
    private var awaitingLiveData: ItemListLiveData? = null
    private var onLastItemCallback: () -> Unit = {}
    private var isLastItemReached = false
    private var isQueryInitialized = false
    private var prevUid: String? = null
    private var lastVisibleItem: DocumentSnapshot? = null
    private val auth by lazy { FirebaseAuth.getInstance() }

    abstract fun createQuery(): Query

    fun reset(onLastItemCallback: () -> Unit = {}) {

        lastVisibleItem = null
        isLastItemReached = false
        prevUid = null
        awaitingLiveData = null
        isQueryInitialized = false
        this.onLastItemCallback = onLastItemCallback
    }

    fun getLiveData(): ItemListLiveData? {

        if (isLastItemReached) {

            return null
        }

        val uid = auth.currentUser?.uid

        if (uid == null) {

            awaitingLiveData = liveData(null, ::setLastVisibleItem, ::onLastItemReached)
            auth.addAuthStateListener(this)

            return awaitingLiveData
        }

        if (!isQueryInitialized) {

            initializeRefs(uid)
            isQueryInitialized = true
        }

        val lastItem = lastVisibleItem

        if (lastItem != null) {

            query = query.startAfter(lastItem)
        }

        return liveData(query, ::setLastVisibleItem, ::onLastItemReached)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {

        val uid = auth.currentUser?.uid

        if (uid != null && uid != prevUid) {

            initializeRefs(uid)
            awaitingLiveData?.setQuery(query)
            prevUid = uid

            auth.removeAuthStateListener(this)
        }
    }

    private fun initializeRefs(uid: String) {

        activitiesRef =
            firestore.collection("/$USERS_COLLECTION/$uid/${ActivitiesConstants.COLLECTION}")

        recordsRef =
            firestore.collection("/$USERS_COLLECTION/$uid/${RecordsConstants.COLLECTION}")

        query = createQuery()
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
