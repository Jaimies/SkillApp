package com.jdevs.timeo.di

import com.jdevs.timeo.data.source.FakeAndroidTestUserManager
import com.jdevs.timeo.data.source.UserManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestAuthModule {

    @Provides
    @Singleton
    fun provideUserManager(): UserManager {

        return FakeAndroidTestUserManager
    }

    @Provides
    @Singleton
    fun provideFakeUserManager(userManager: UserManager): FakeAndroidTestUserManager {

        return userManager as FakeAndroidTestUserManager
    }
}