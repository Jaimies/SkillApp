package com.maxpoliakov.skillapp.data.di

import android.content.Context
import android.content.SharedPreferences
import com.maxpoliakov.skillapp.data.persistence.sharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.sharedPrefs
    }
}
