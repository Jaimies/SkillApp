package com.jdevs.timeo.data.firestore

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class DocumentLiveData<I : Any, O : Any>(
    private val reference: DocumentReference,
    private val modelClass: Class<I>,
    private val mapFunction: (I) -> O
) : LiveData<O>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {

        listener = reference.addSnapshotListener { snapshot, _ ->
            kotlin.runCatching {
                snapshot?.toObject(modelClass)?.let { value = mapFunction(it) }
            }
        }
    }

    override fun onInactive() {
        listener?.remove()
        listener = null
    }
}

class QueryLiveData<I : Any, O : Any>(
    private val query: Query,
    private val modelClass: Class<I>,
    private val mapFunction: (I) -> O
) : LiveData<List<O>>() {

    private var listener: ListenerRegistration? = null

    override fun onActive() {

        listener = query.addSnapshotListener { snapshot, _ ->
            kotlin.runCatching {
                snapshot?.toObjects(modelClass)?.let { value = it.map(mapFunction) }
            }
        }
    }

    override fun onInactive() {
        listener?.remove()
        listener = null
    }
}

inline fun <reified I : Any, O : Any> Query.watch(noinline mapFunction: (I) -> O): LiveData<List<O>> {

    return QueryLiveData(this, I::class.java, mapFunction)
}

inline fun <reified I : Any, O : Any> DocumentReference.watch(noinline mapFunction: (I) -> O): LiveData<O> {

    return DocumentLiveData(this, I::class.java, mapFunction)
}
