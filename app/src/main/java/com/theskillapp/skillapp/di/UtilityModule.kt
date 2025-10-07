package com.theskillapp.skillapp.di

import com.theskillapp.skillapp.domain.repository.NotificationUtil
import com.theskillapp.skillapp.shared.notifications.NotificationUtilImpl
import com.theskillapp.skillapp.ui.intro.IntroUtil
import com.theskillapp.skillapp.ui.intro.IntroUtilImpl
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
