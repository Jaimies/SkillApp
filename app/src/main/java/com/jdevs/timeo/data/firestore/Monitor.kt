package com.jdevs.timeo.data.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME
import kotlin.reflect.KClass

fun <I : Any, O> CollectionReference.monitorCollection(
    type: KClass<I>,
    mapper: Mapper<I, O>,
    limit: Long,
    orderBy: String = TOTAL_TIME,
    direction: Query.Direction = Query.Direction.DESCENDING
): LiveData<List<O>> {

    val liveData = MutableLiveData<List<O>>()

    orderBy(orderBy, direction).limit(limit)
        .addSnapshotListener { querySnapshot, _ ->

            querySnapshot?.documents?.mapNotNull {
                it.toObject(type.java)
            }?.let { liveData.value = it.map(mapper::map) }
        }

    return liveData
}

fun <I : Any, O> CollectionReference.monitorDocument(
    documentId: String,
    type: KClass<I>,
    mapper: Mapper<I, O>
): LiveData<O> {

    val liveData = MutableLiveData<O>()

    document(documentId).addSnapshotListener { documentSnapshot, _ ->

        documentSnapshot?.toObject(type.java)?.let { liveData.value = mapper.map(it) }
    }

    return liveData
}
