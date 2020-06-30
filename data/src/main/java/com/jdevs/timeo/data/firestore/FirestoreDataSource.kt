package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jdevs.timeo.domain.repository.AuthRepository
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class FirestoreDataSource(private val authRepository: AuthRepository) {

    protected val db = Firebase.firestore
    private var mUid = ""

    protected abstract fun resetRefs(uid: String)

    protected fun reset() {

         val uid = authRepository.uid
        if (uid == null || uid == mUid) return

        resetRefs(uid)
        mUid = uid
    }

    fun <T> T.safeAccess(): T {
        reset()
        return this
    }

    inner class SafeAccess<T : Any> : ReadWriteProperty<Any?, T> {
        private val fieldHolder = FieldHolder<T>()

        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            fieldHolder.safeAccess().field

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            fieldHolder.field = value
        }

        inner class FieldHolder<T : Any> {
            lateinit var field: T
        }
    }
}

abstract class FirestoreListDataSource(authRepository: AuthRepository) :
    FirestoreDataSource(authRepository) {

    protected fun createRef(
        uid: String, collection: String,
        watcher: QueryWatcher<*, *>? = null
    ) = db.collection("/users/$uid/$collection").also { watcher?.setQuery(it) }
}
