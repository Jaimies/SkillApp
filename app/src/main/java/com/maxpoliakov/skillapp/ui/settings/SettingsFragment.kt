package com.maxpoliakov.skillapp.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.analytics.setAsCurrentScreen
import com.maxpoliakov.skillapp.util.dialog.showSnackbar
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        val themePref = findPreference<ListPreference>("theme")!!

        themePref.setOnPreferenceChangeListener { _, newValue ->
            setTheme(Theme.valueOf(newValue as String))
            logEvent("change_theme")
            true
        }

        val backupsPref = findPreference<Preference>("backups")!!

        if (!viewModel.subscriptionState.value.hasAccessToPremium)
            backupsPref.widgetLayoutResource = R.layout.premium_widget

        lifecycleScope.launch {
            viewModel.subscriptionState.collect { state ->
                backupsPref.widgetLayoutResource =
                    if (state.hasAccessToPremium) R.layout.empty_widget
                    else R.layout.premium_widget
            }
        }

        backupsPref.setOnPreferenceClickListener {
            val controller = findNavController()

            val destination = if (viewModel.subscriptionState.value.hasAccessToPremium)
                R.id.backup_fragment_dest else
                R.id.premium_fragment_dest

            controller.navigateAnimated(destination)
            true
        }

        val premiumPref = findPreference<Preference>("premium")!!

        premiumPref.setOnPreferenceClickListener {
            findNavController().navigateAnimated(R.id.premium_fragment_dest)
            true
        }

        val rateTheAppPref = findPreference<Preference>("rate_the_app")!!

        rateTheAppPref.setOnPreferenceClickListener {
            openUri(Intent.ACTION_VIEW, R.string.market_uri, R.string.cant_open_google_play)
            true
        }

        val emailPref = findPreference<Preference>("contact_developer")!!

        emailPref.setOnPreferenceClickListener {
            openUri(Intent.ACTION_SENDTO, R.string.mail_dev_uri, R.string.mail_app_not_found)
            true
        }
    }

    private fun openUri(action: String, uriResId: Int, errorMessageResId: Int) {
        val intent = Intent(action).apply {
            data = Uri.parse(getString(uriResId))
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            showSnackbar(errorMessageResId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.setPadding(12.dp.toPx(requireContext()), 0, 0, 0)
    }

    override fun onResume() {
        super.onResume()
        setAsCurrentScreen()
    }
}
