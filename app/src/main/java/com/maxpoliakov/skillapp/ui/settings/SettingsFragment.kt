package com.maxpoliakov.skillapp.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.shared.Dimension.Companion.dp
import com.maxpoliakov.skillapp.shared.dialog.showSnackbar
import com.maxpoliakov.skillapp.shared.extensions.navigateAnimated
import com.maxpoliakov.skillapp.shared.extensions.setTheme
import com.maxpoliakov.skillapp.shared.fragment.showTimePicker
import com.maxpoliakov.skillapp.shared.settings.DayStartTimeSummaryProvider
import com.maxpoliakov.skillapp.shared.settings.TimePickerPreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        findPreference<ListPreference>("theme")!!.setOnPreferenceChangeListener { _, newValue ->
            setTheme(Theme.valueOf(newValue as String))
            true
        }

        setOnPreferenceClickedListener("google_drive_backup", failIfPreferenceNotFound = true) {
            findNavController().navigateAnimated(R.id.google_drive_backup_fragment_dest)
        }

        setOnPreferenceClickedListener("local_backup", failIfPreferenceNotFound = true) {
            findNavController().navigateAnimated(R.id.shared_storage_backup_fragment_dest)
        }

        findPreference<Preference>("day_start_time")!!.summaryProvider = DayStartTimeSummaryProvider()

        setOnPreferenceClickedListener("source_code") {
            openUri(Intent.ACTION_VIEW, R.string.source_code_url, R.string.browser_not_found)
        }

        setOnPreferenceClickedListener("support_app") {
            openUri(Intent.ACTION_VIEW, R.string.support_app_uri, R.string.browser_not_found)
        }

        setOnPreferenceClickedListener("rate_the_app") {
            openUri(Intent.ACTION_VIEW, R.string.market_uri, R.string.cant_open_google_play)
        }

        setOnPreferenceClickedListener("privacy_policy") {
            openUri(Intent.ACTION_VIEW, R.string.privacy_policy_url, R.string.browser_not_found)
        }

        setOnPreferenceClickedListener("contact_developer") {
            openUri(Intent.ACTION_SENDTO, R.string.mail_dev_uri, R.string.mail_app_not_found)
        }
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference !is TimePickerPreference) {
            return super.onDisplayPreferenceDialog(preference)
        }

        childFragmentManager.showTimePicker(
            requireContext(),
            R.string.select_day_start_time,
            preference.value,
        ) { time ->
            preference.value = time
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

    private fun setOnPreferenceClickedListener(
        preferenceName: String,
        failIfPreferenceNotFound: Boolean = false,
        onClicked: () -> Unit,
    ) {
        val preference = findPreference<Preference>(preferenceName)

        if (failIfPreferenceNotFound) require(preference != null)

        findPreference<Preference>(preferenceName)?.setOnPreferenceClickListener {
            onClicked()
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.setPadding(12.dp.toPx(requireContext()), 0, 0, 0)
    }
}
