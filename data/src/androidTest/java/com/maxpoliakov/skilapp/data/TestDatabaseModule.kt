package com.maxpoliakov.skilapp.data

import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DatabaseModule::class])
class TestDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase {
        return createTestDatabase()
    }

    @Provides
    fun provideSkillDao(db: AppDatabase) = db.skillDao()

    @Provides
    fun provideStatsDao(db: AppDatabase) = db.statsDao()

    @Provides
    fun provideRecordsDao(db: AppDatabase) = db.recordsDao()

    @Provides
    fun provideSkillGroupDao(db: AppDatabase) = db.skillGroupDao()
}
