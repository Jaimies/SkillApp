package com.jdevs.timeo.data.firestore

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.Query

class QueryLiveData<I : Any, O : Any>(
    private val query: Query,
    private val modelClass: Class<I>,
    private val mapFunction: (I) -> O
) : FirestoreListenerLiveData<List<O>>() {

    override fun registerListener() = query.addSnapshotListener { snapshot, _ ->
        kotlin.runCatching {
            snapshot?.toObjects(modelClass)?.let { value = it.map(mapFunction) }
        }
    }
}

inline fun <reified I : Any, O : Any> Query.watch(noinline mapFunction: (I) -> O): LiveData<List<O>> {
    return QueryLiveData(this, I::class.java, mapFunction)
}
