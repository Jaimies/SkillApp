package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.util.notifications.NotificationUtil
import com.maxpoliakov.skillapp.util.notifications.NotificationUtilImpl
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchPersistence
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchPersistenceImpl
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UtilityModule {
    @Binds
    fun provideStopwatchUtil(stopwatchUtil: StopwatchUtilImpl): StopwatchUtil

    @Binds
    fun provideStopwatchPersistence(persistence: StopwatchPersistenceImpl): StopwatchPersistence

    @Binds
    fun provideNotificationUtil(notificationUtil: NotificationUtilImpl): NotificationUtil
}
