package com.maxpoliakov.skillapp.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BaseModule {
    @Provides
    @Singleton
    fun provideIoScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }
}
