package com.jdevs.timeo.di

import com.jdevs.timeo.ui.activities.ActivitiesFragmentTest
import com.jdevs.timeo.ui.activitydetail.ActivityDetailFragmentTest
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
@Component(modules = [TestRepositoryModule::class])
interface TestAppComponent : AppComponent {

    fun inject(test: ActivityDetailFragmentTest)
    fun inject(test: ActivitiesFragmentTest)
}
