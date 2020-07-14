package com.jdevs.timeo.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.jdevs.timeo.R
import com.jdevs.timeo.util.fragment.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activitiesEnabled = findPreference<SwitchPreferenceCompat>("activitiesEnabled")!!
        observe(viewModel.activitiesEnabled, activitiesEnabled::setChecked)

        activitiesEnabled.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setActivitiesEnabled(newValue as Boolean)
            true
        }
    }
}
