package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.data.AndroidNetworkUtil
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.util.notifications.NotificationUtilImpl
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
    fun provideNetworkUtil(networkUtil: AndroidNetworkUtil): NetworkUtil
}
