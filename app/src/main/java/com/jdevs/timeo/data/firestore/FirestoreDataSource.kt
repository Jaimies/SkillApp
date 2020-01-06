package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.auth.AuthRepository
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class FirestoreDataSource(
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

    companion object {

        private const val USERS_COLLECTION = "users"
    }
}
