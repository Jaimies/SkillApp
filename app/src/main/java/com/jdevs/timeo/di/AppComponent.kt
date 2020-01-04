package com.jdevs.timeo.di

import android.content.Context
import com.jdevs.timeo.ui.activities.ActivitiesFragment
import com.jdevs.timeo.ui.activitydetail.ActivityDetailFragment
import com.jdevs.timeo.ui.addactivity.AddEditActivityFragment
import com.jdevs.timeo.ui.history.HistoryFragment
import com.jdevs.timeo.ui.profile.ProfileFragment
import com.jdevs.timeo.ui.signin.SignInFragment
import com.jdevs.timeo.ui.signin.SignUpFragment
import com.jdevs.timeo.ui.stats.StatsItemFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, AuthModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: ActivitiesFragment)
    fun inject(fragment: AddEditActivityFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: ActivityDetailFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SignInFragment)
    fun inject(fragment: SignUpFragment)
    fun inject(fragment: StatsItemFragment)
}
