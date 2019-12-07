package com.jdevs.timeo.api.repository.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.api.livedata.ItemListLiveData
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.USERS_COLLECTION

abstract class ItemListRepository(private val onLastItemCallback: () -> Unit = {}) :
    FirebaseAuth.AuthStateListener {

    protected lateinit var activitiesRef: CollectionReference
    protected lateinit var recordsRef: CollectionReference
    protected abstract val liveData: (Query?, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemListLiveData
    protected val firestore by lazy { FirebaseFirestore.getInstance() }

    private lateinit var query: Query
    private lateinit var awaitingLiveData: ItemListLiveData
    private var isLastItemReached = false
    private var prevUid: String? = null
    private var lastVisibleItem: DocumentSnapshot? = null
    private val auth by lazy { FirebaseAuth.getInstance() }

    abstract fun createQuery(): Query

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

        if (!::query.isInitialized) {

            initializeRefs(uid)
            query = createQuery()
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
            awaitingLiveData.setQuery(createQuery())

            prevUid = uid

            auth.removeAuthStateListener(this)
        }
    }

    private fun initializeRefs(uid: String) {

        activitiesRef =
            firestore.collection("/$USERS_COLLECTION/$uid/${ActivitiesConstants.COLLECTION}")

        recordsRef =
            firestore.collection("/$USERS_COLLECTION/$uid/${RecordsConstants.COLLECTION}")
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
