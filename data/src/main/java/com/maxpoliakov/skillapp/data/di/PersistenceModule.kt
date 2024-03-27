package com.maxpoliakov.skillapp.data.di

import android.content.Context
import android.content.SharedPreferences
import com.maxpoliakov.skillapp.data.extensions.sharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.sharedPrefs
    }
}
