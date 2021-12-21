package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.data.PremiumUtilImpl
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.PremiumUtil
import com.maxpoliakov.skillapp.util.network.NetworkUtil
import com.maxpoliakov.skillapp.util.network.NetworkUtilImpl
import com.maxpoliakov.skillapp.util.notifications.NotificationUtilImpl
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import com.maxpoliakov.skillapp.util.tracking.RecordUtilImpl
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
    fun provideNetworkUtil(networkUtil: NetworkUtilImpl): NetworkUtil

    @Binds
    fun provideRecordUtil(recordUtil: RecordUtilImpl): RecordUtil

    @Binds
    fun providePremiumUtil(premiumUtil: PremiumUtilImpl): PremiumUtil
}
