package com.jdevs.timeo.data.util

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch

suspend fun FirebaseFirestore.runBatchSuspend(batchFunction: suspend (WriteBatch) -> Unit) {
    val batch = this.batch()
    batchFunction(batch)
    batch.commit()
}
