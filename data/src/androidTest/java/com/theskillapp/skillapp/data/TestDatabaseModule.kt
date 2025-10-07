package com.theskillapp.skillapp.data

import com.theskillapp.skillapp.data.db.AppDatabase
import com.theskillapp.skillapp.data.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DatabaseModule::class])
object TestDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase {
        return createTestDatabase()
    }
}
