package com.theskillapp.skillapp.screenshots

import com.theskillapp.skillapp.di.UtilityModule
import com.theskillapp.skillapp.domain.repository.NotificationUtil
import com.theskillapp.skillapp.shared.notifications.NotificationUtilImpl
import com.theskillapp.skillapp.ui.intro.IntroUtil
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
