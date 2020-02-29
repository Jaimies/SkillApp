package com.jdevs.timeo.data.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.jdevs.timeo.data.TOTAL_TIME

inline fun <reified I : Any, O> CollectionReference.watchCollection(
    crossinline mapFunction: (I) -> O,
    limit: Long,
    orderBy: String = TOTAL_TIME,
    direction: Query.Direction = Query.Direction.DESCENDING,
    query: CollectionReference.() -> Query = { this }
): LiveData<List<O>> {

    val liveData = MutableLiveData<List<O>>()

    query().orderBy(orderBy, direction).limit(limit)
        .addSnapshotListener { querySnapshot, _ ->

            querySnapshot?.toObjects<I>()?.let { list ->

                liveData.value = list.map(mapFunction)
            }
        }

    return liveData
}

inline fun <reified I : Any, O> DocumentReference.watch(crossinline mapFunction: (I) -> O): LiveData<O> {

    val liveData = MutableLiveData<O>()

    addSnapshotListener { documentSnapshot, _ ->

        documentSnapshot?.toObject<I>()?.let { item ->

            liveData.value = mapFunction(item)
        }
    }

    return liveData
}
