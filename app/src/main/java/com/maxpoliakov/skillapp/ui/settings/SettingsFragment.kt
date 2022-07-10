package com.maxpoliakov.skillapp.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
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

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        findPreference<ListPreference>("theme")!!.setOnPreferenceChangeListener { _, newValue ->
            setTheme(Theme.valueOf(newValue as String))
            logEvent("change_theme")
            true
        }

        val backupsPref = findPreference<Preference>("backups")!!

        backupsPref.setOnPreferenceClickListener {
            findNavController().navigateAnimated(R.id.backup_fragment_dest)
            true
        }

        setOnPreferenceClickedListener("rate_the_app") {
            openUri(Intent.ACTION_VIEW, R.string.market_uri, R.string.cant_open_google_play)
        }

        setOnPreferenceClickedListener("contact_developer") {
            openUri(Intent.ACTION_SENDTO, R.string.mail_dev_uri, R.string.mail_app_not_found)
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

    private fun setOnPreferenceClickedListener(preferenceName: String, onClicked: () -> Unit) {
        findPreference<Preference>(preferenceName)?.setOnPreferenceClickListener {
            onClicked()
            true
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
