package com.jdevs.timeo.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.jdevs.timeo.R
import com.jdevs.timeo.di.ViewModelFactory
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.observe
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var activitiesEnabled: SwitchPreferenceCompat

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.settings, rootKey)

        activitiesEnabled = findPreference("activitiesEnabled")!!
        activitiesEnabled.setOnPreferenceChangeListener { _, newValue ->

            viewModel.setActivitiesEnabled(newValue as Boolean)
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.activitiesEnabled) { newValue ->

            activitiesEnabled.isChecked = newValue
        }
    }
}
