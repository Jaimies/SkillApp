package com.theskillapp.skillapp.data

import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase

fun Cursor.getLongValue(name: String) = getLong(getColumnIndex(name))
fun Cursor.getIntValue(name: String) = getInt(getColumnIndex(name))
fun Cursor.getStringValue(name: String) = getString(getColumnIndex(name))

fun SupportSQLiteDatabase.queryASingleItem(queryString: String, action: (Cursor) -> Unit) {
    val cursor = query(queryString)
    cursor.moveToFirst()
    action(cursor)
    cursor.close()
}
