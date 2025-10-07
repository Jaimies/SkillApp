package com.theskillapp.skillapp.data

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.theskillapp.skillapp.data.db.AppDatabase

fun createTestDatabase(): AppDatabase {
    return Room.inMemoryDatabaseBuilder(
        getContext(), AppDatabase::class.java
    ).build()
}

private fun getContext() : Context {
    return InstrumentationRegistry.getInstrumentation().targetContext
}
