package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.util.notifications.NotificationUtil
import com.maxpoliakov.skillapp.util.notifications.NotificationUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface NotificationModule {
    @Binds
    fun provideNotificationUtil(notificationUtil: NotificationUtilImpl): NotificationUtil
}
