package com.jdevs.timeo.data.firestore

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ListenerRegistration

abstract class FirestoreListenerLiveData<T> : LiveData<T>() {

    private var listener: ListenerRegistration? = null

    @CallSuper
    override fun onActive() {
        listener = registerListener()
    }

    @CallSuper
    override fun onInactive() {
        listener?.remove()
        listener = null
    }

    abstract fun registerListener(): ListenerRegistration
}
