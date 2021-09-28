package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.di.UtilityModule
import com.maxpoliakov.skillapp.util.network.NetworkUtil
import com.maxpoliakov.skillapp.util.network.NetworkUtilImpl
import com.maxpoliakov.skillapp.util.notifications.NotificationUtil
import com.maxpoliakov.skillapp.util.notifications.NotificationUtilImpl
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchPersistence
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchPersistenceImpl
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
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
    fun provideStopwatchUtil(stopwatchUtil: StubStopwatchUtil): StopwatchUtil

    @Binds
    fun provideStopwatchPersistence(persistence: StopwatchPersistenceImpl): StopwatchPersistence

    @Binds
    fun provideNotificationUtil(notificationUtil: NotificationUtilImpl): NotificationUtil

    @Binds
    fun provideNetworkUtil(networkUtil: NetworkUtilImpl): NetworkUtil
}
