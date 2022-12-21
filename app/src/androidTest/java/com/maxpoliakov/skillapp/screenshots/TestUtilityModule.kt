package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.di.UtilityModule
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.util.network.NetworkUtil
import com.maxpoliakov.skillapp.util.network.NetworkUtilImpl
import com.maxpoliakov.skillapp.util.notifications.NotificationUtilImpl
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import com.maxpoliakov.skillapp.util.tracking.RecordUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UtilityModule::class],
)
interface TestUtilityModule {
    @Binds
    fun provideNotificationUtil(notificationUtil: NotificationUtilImpl): NotificationUtil

    @Binds
    fun provideNetworkUtil(networkUtil: NetworkUtilImpl): NetworkUtil
}
