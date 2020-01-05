package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.source.AuthRepository

abstract class BaseRemoteDataSource(
    private val authRepository: AuthRepository
) {

    protected val db = FirebaseFirestore.getInstance()
    private var prevUid = ""

    protected abstract fun resetRefs(uid: String)

    protected fun reset() {

        val uid = authRepository.uid ?: return

        if (uid == prevUid) {

            return
        }

        resetRefs(uid)

        prevUid = uid
    }

    fun createRef(uid: String, collection: String, monitor: CollectionMonitor) =
        db.collection("/$USERS_COLLECTION/$uid/$collection").also {

            monitor.setRef(it)
        }

    companion object {

        private const val USERS_COLLECTION = "users"
    }
}
