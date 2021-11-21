package com.maxpoliakov.skillapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.SubscriptionState
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.analytics.setAsCurrentScreen
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    @Inject
    lateinit var billingRepository: BillingRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        val themePref = findPreference<ListPreference>("theme")!!

        themePref.setOnPreferenceChangeListener { _, newValue ->
            setTheme(Theme.valueOf(newValue as String))
            logEvent("change_theme")
            true
        }

        val backupsPref = findPreference<Preference>("backups")!!

        if (!billingRepository.isSubscribed.value)
            backupsPref.widgetLayoutResource = R.layout.premium_widget

        lifecycleScope.launch {
            billingRepository.isSubscribed.collect { isSubscribed ->
                backupsPref.widgetLayoutResource =
                    if (isSubscribed) R.layout.empty_widget else R.layout.premium_widget
            }
        }

        backupsPref.setOnPreferenceClickListener {
            val controller = findNavController()

            val destination =
                if (billingRepository.isSubscribed.value) R.id.backup_fragment_dest
                else R.id.premium_fragment_dest

            controller.navigateAnimated(destination)
            true
        }

        val premiumPref = findPreference<Preference>("premium")!!

        premiumPref.setOnPreferenceClickListener {
            findNavController().navigateAnimated(R.id.premium_fragment_dest)
            true
        }

        lifecycleScope.launch {
            billingRepository.subscriptionState.collect { state ->
                backupsPref.isVisible = state != SubscriptionState.Loading
                premiumPref.isVisible = state != SubscriptionState.Loading
            }
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
