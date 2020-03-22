package com.jdevs.timeo.data.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.jdevs.timeo.data.TOTAL_TIME

inline fun <reified I : Any, O> Query.watchCollection(
    crossinline mapFunction: (I) -> O,
    limit: Long,
    orderBy: String = TOTAL_TIME,
    direction: Query.Direction = Query.Direction.DESCENDING
): LiveData<List<O>> {

    val liveData = MutableLiveData<List<O>>()

    orderBy(orderBy, direction).limit(limit).addSnapshotListener { querySnapshot, _ ->

        kotlin.runCatching {

            querySnapshot?.toObjects<I>()?.let { list ->
                liveData.value = list.map(mapFunction)
            }
        }
    }

    return liveData
}

inline fun <reified I : Any, O> DocumentReference.watch(crossinline mapFunction: (I) -> O): LiveData<O> {

    val liveData = MutableLiveData<O>()

    addSnapshotListener { documentSnapshot, _ ->

        kotlin.runCatching {

            documentSnapshot?.toObject<I>()?.let { liveData.value = mapFunction(it) }
        }
    }

    return liveData
}
