package com.jdevs.timeo.data.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.TOTAL_TIME

inline fun <reified I : Any, O> CollectionReference.watchCollection(
    crossinline mapper: (I) -> O,
    limit: Long,
    orderBy: String = TOTAL_TIME,
    direction: Query.Direction = Query.Direction.DESCENDING
): LiveData<List<O>> {

    val liveData = MutableLiveData<List<O>>()

    orderBy(orderBy, direction).limit(limit)
        .addSnapshotListener { querySnapshot, _ ->

            querySnapshot?.documents?.mapNotNull {
                it.toObject(I::class.java)
            }?.let { liveData.value = it.map(mapper) }
        }

    return liveData
}

inline fun <reified I : Any, O> DocumentReference.watch(crossinline mapper: (I) -> O): LiveData<O> {

    val liveData = MutableLiveData<O>()

    addSnapshotListener { documentSnapshot, _ ->

        documentSnapshot?.toObject(I::class.java)?.let { liveData.value = mapper(it) }
    }

    return liveData
}
