package com.theskillapp.skillapp.data.extensions

import android.database.Cursor

fun <T> Cursor.toList(getItem: (Cursor) -> T) : List<T> {
    return generateSequence { if (this.moveToNext()) this else null }
        .map(getItem)
        .toList()
}

fun <T> Cursor.useToConvertToList(getItem: (Cursor) -> T): List<T> {
    return this.use {
        this.toList(getItem)
    }
}
