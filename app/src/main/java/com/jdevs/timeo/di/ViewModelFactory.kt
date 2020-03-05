package com.jdevs.timeo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdevs.timeo.ui.activities.ActivitiesViewModel
import com.jdevs.timeo.ui.activitydetail.ActivityDetailViewModel
import com.jdevs.timeo.ui.addactivity.AddEditActivityViewModel
import com.jdevs.timeo.ui.addproject.AddEditProjectViewModel
import com.jdevs.timeo.ui.history.HistoryViewModel
import com.jdevs.timeo.ui.overview.OverviewViewModel
import com.jdevs.timeo.ui.profile.ProfileViewModel
import com.jdevs.timeo.ui.projectdetail.ProjectDetailViewModel
import com.jdevs.timeo.ui.projects.ProjectsViewModel
import com.jdevs.timeo.ui.settings.SettingsViewModel
import com.jdevs.timeo.ui.signin.SignInViewModel
import com.jdevs.timeo.ui.signin.SignUpViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return viewModels[modelClass]?.get() as T
    }
}

@MapKey
@Retention
@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module(includes = [ProfileViewModelsModule::class, OverviewViewModelsModule::class])
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Module
abstract class OverviewViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel::class)
    abstract fun bindOverviewViewModel(viewModel: OverviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProjectsViewModel::class)
    abstract fun bindProjectsViewModel(viewModel: ProjectsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProjectViewModel::class)
    abstract fun bindAddProjectViewModel(viewModel: AddEditProjectViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProjectDetailViewModel::class)
    abstract fun bindProjectDetailViewModel(viewModel: ProjectDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActivitiesViewModel::class)
    abstract fun bindActivitiesViewModel(viewModel: ActivitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditActivityViewModel::class)
    abstract fun bindAddActivityViewModel(viewModel: AddEditActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindHistoryViewModel(viewModel: HistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActivityDetailViewModel::class)
    abstract fun bindActivityDetailViewModel(viewModel: ActivityDetailViewModel): ViewModel
}

@Module
abstract class ProfileViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}
