package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.shared.notifications.NotificationUtilImpl
import com.maxpoliakov.skillapp.ui.intro.IntroUtil
import com.maxpoliakov.skillapp.ui.intro.IntroUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UtilityModule {
    @Binds
    fun provideNotificationUtil(notificationUtil: NotificationUtilImpl): NotificationUtil

    @Binds
    fun provideIntroUtil(introUtil: IntroUtilImpl): IntroUtil
}
