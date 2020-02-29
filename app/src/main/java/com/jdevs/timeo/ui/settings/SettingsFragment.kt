package com.jdevs.timeo.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.jdevs.timeo.R
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.observe
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var viewModel: SettingsViewModel
    private lateinit var activitiesEnabled: SwitchPreference

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
