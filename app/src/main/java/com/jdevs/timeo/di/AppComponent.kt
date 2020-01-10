package com.jdevs.timeo.di

import android.content.Context
import com.jdevs.timeo.ui.activities.ActivitiesFragment
import com.jdevs.timeo.ui.activitydetail.ActivityDetailFragment
import com.jdevs.timeo.ui.addactivity.AddEditActivityFragment
import com.jdevs.timeo.ui.addproject.AddEditProjectFragment
import com.jdevs.timeo.ui.history.HistoryFragment
import com.jdevs.timeo.ui.profile.ProfileFragment
import com.jdevs.timeo.ui.projectdetail.ProjectDetailFragment
import com.jdevs.timeo.ui.projects.ProjectsFragment
import com.jdevs.timeo.ui.signin.SignInFragment
import com.jdevs.timeo.ui.signin.SignUpFragment
import com.jdevs.timeo.ui.stats.StatsItemFragment
import com.jdevs.timeo.ui.summary.SummaryFragment
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

    fun inject(fragment: SummaryFragment)
    fun inject(fragment: ActivitiesFragment)
    fun inject(fragment: ProjectsFragment)
    fun inject(fragment: AddEditActivityFragment)
    fun inject(fragment: AddEditProjectFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: ActivityDetailFragment)
    fun inject(fragment: ProjectDetailFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SignInFragment)
    fun inject(fragment: SignUpFragment)
    fun inject(fragment: StatsItemFragment)
}
