package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.di.UtilityModule
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.shared.notifications.NotificationUtilImpl
import com.maxpoliakov.skillapp.ui.intro.IntroUtil
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
    fun provideIntroUtil(introUtil: StubIntroUtil): IntroUtil
}
