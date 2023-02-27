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
    @Singleton
    fun provideSkillDao(db: AppDatabase) = db.skillDao()

    @Provides
    @Singleton
    fun provideStatsDao(db: AppDatabase) = db.statsDao()

    @Provides
    @Singleton
    fun provideRecordsDao(db: AppDatabase) = db.recordsDao()

    @Provides
    @Singleton
    fun provideSkillGroupDao(db: AppDatabase) = db.skillGroupDao()
}
