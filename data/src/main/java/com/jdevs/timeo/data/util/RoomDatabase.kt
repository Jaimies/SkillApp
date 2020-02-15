package com.jdevs.timeo.data.util

import androidx.room.RoomDatabase

@Suppress("deprecation")
internal suspend inline fun RoomDatabase.runInTransactionSuspend(crossinline body: suspend () -> Unit) {

    beginTransaction()
    try {
        body.invoke()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}
