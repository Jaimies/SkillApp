package com.jdevs.timeo.data.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source.CACHE
import com.google.firebase.firestore.Source.SERVER
import com.google.firebase.firestore.WriteBatch
import kotlinx.coroutines.tasks.await

suspend fun FirebaseFirestore.runSuspendBatch(batchFunction: suspend (WriteBatch) -> Unit) {
    val batch = batch()
    batchFunction(batch)
    batch.commit()
}

fun Query.whereInRange(field: String, from: Any, to: Any): Query {
    return this
        .whereGreaterThan(field, from)
        .whereLessThan(field, to)
}

suspend fun DocumentReference.getCacheFirst(): DocumentSnapshot {
    return runCatching { get(CACHE).await() }.getOrNull() ?: get(SERVER).await()
}

fun increment(i: Int) = FieldValue.increment(i.toLong())
