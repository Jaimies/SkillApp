package com.maxpoliakov.skillapp.screenshots

import android.content.Context
import androidx.room.Room
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class],
)
class TestDbModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideSkillDao(db: AppDatabase) = db.skillDao()

    @Provides
    @Singleton
    fun provideStatsDao(db: AppDatabase) = db.statsDao()

    @Provides
    @Singleton
    fun provideRecordsDao(db: AppDatabase) = db.recordsDao()
}
