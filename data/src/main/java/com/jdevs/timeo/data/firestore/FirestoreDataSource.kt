package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jdevs.timeo.domain.repository.AuthRepository

abstract class FirestoreDataSource(authRepository: AuthRepository) {
    protected val db = Firebase.firestore

    init {
        authRepository.uid.value?.let { resetRefs(it) }
        authRepository.uid.observeForever { uid ->
            uid?.let(::resetRefs)
        }
    }

    protected abstract fun resetRefs(uid: String)
}

abstract class FirestoreListDataSource(authRepository: AuthRepository) :
    FirestoreDataSource(authRepository) {
    protected fun createRef(
        uid: String, collection: String,
        watcher: QueryWatcher<*, *>? = null
    ) = db.collection("/users/$uid/$collection").also { watcher?.setQuery(it) }
}
