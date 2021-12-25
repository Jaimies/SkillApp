package com.maxpoliakov.skillapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.SubscriptionState.Subscribed
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.analytics.setAsCurrentScreen
import com.maxpoliakov.skillapp.util.dialog.showSnackbar
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var adPref: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadAdIfPossible(requireContext())
    }

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

        adPref = findPreference("premium_ad")!!

        adPref.setOnPreferenceClickListener {
            viewModel.onWatchAdClicked(requireActivity())
            true
        }

        lifecycleScope.launchWhenCreated {
            viewModel.subscriptionState.collect { state ->
                adPref.isVisible = state != Subscribed
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.setPadding(12.dp.toPx(requireContext()), 0, 0, 0)
        observe(viewModel.showSnackbar) { message -> showSnackbar(message) }
        observe(viewModel.timeTillFreeSubscriptionExpires) {}
        observe(viewModel.timeTillFreeSubscriptionExpires) { timeTillExpiry ->
            adPref.summary = if (timeTillExpiry != null)
                getString(R.string.premium_ad_with_free_subscription_summary, timeTillExpiry) else
                getString(R.string.premium_ad_summary)
        }
    }

    override fun onResume() {
        super.onResume()
        setAsCurrentScreen()
    }
}
