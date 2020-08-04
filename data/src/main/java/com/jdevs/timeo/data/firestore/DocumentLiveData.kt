package com.jdevs.timeo.data.firestore

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference

class DocumentLiveData<I : Any, O : Any>(
    private val documentRef: DocumentReference,
    private val modelClass: Class<I>,
    private val mapFunction: (I) -> O
) : FirestoreListenerLiveData<O>() {

    override fun registerListener() = documentRef.addSnapshotListener { snapshot, _ ->
        kotlin.runCatching {
            snapshot?.toObject(modelClass)?.let { value = mapFunction(it) }
        }
    }
}

inline fun <reified I : Any, O : Any> DocumentReference.watch(noinline mapFunction: (I) -> O): LiveData<O> {
    return DocumentLiveData(this, I::class.java, mapFunction)
}
