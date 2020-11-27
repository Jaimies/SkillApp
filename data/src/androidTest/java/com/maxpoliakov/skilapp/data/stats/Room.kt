package com.maxpoliakov.skilapp.data.stats

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.maxpoliakov.skillapp.data.db.AppDatabase

fun createTestDatabase(): AppDatabase {
    return Room.inMemoryDatabaseBuilder(
        getContext(), AppDatabase::class.java
    ).build()
}

private fun getContext() : Context {
    return InstrumentationRegistry.getInstrumentation().targetContext
}
