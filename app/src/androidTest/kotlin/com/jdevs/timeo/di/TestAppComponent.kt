package com.jdevs.timeo.di

import com.jdevs.timeo.ui.activities.ActivitiesFragmentTest
import com.jdevs.timeo.ui.activitydetail.ActivityDetailFragmentTest
import com.jdevs.timeo.ui.history.HistoryFragmentTest
import com.jdevs.timeo.ui.profile.ProfileFragmentTest
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
@Component(modules = [TestRepositoryModule::class, TestAuthModule::class, ViewModelModule::class])
interface TestAppComponent : AppComponent {

    fun inject(test: ActivityDetailFragmentTest)
    fun inject(test: ActivitiesFragmentTest)
    fun inject(test: HistoryFragmentTest)
    fun inject(test: ProfileFragmentTest)
}
