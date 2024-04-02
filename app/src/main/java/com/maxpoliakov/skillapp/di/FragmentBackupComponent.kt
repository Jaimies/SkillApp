package com.maxpoliakov.skillapp.di

import androidx.fragment.app.Fragment
import com.maxpoliakov.skillapp.ui.restore.RestoreBackupFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ActivityModule::class,
        LifecycleModule::class,
        SnackbarModule::class,
        FragmentModule::class,
    ]
)
interface FragmentBackupComponent {
    fun inject(restoreBackupFragment: RestoreBackupFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): FragmentBackupComponent
    }
}
