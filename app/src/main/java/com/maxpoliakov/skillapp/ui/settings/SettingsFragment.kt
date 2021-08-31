package com.maxpoliakov.skillapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.billingclient.api.BillingFlowParams
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.billing.BillingRepository
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setTheme
import dagger.hilt.android.AndroidEntryPoint
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
            true
        }

        val backupsPref = findPreference<Preference>("backups")!!

        backupsPref.setOnPreferenceClickListener {
            val controller = findNavController()
            controller.navigateAnimated(R.id.backup_fragment_dest)
            true
        }

        val premiumPref = findPreference<Preference>("premium")!!

        premiumPref.setOnPreferenceClickListener {
            lifecycleScope.launch { showBillingPrompt() }
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.setPadding(12.dp.toPx(requireContext()), 0, 0, 0)
    }

    private suspend fun showBillingPrompt() {
        val skuDetails = billingRepository.getSkuDetails()

        if (skuDetails.isEmpty()) return

        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails[0])
            .build()

        billingRepository.billingClient.launchBillingFlow(requireActivity(), flowParams)
    }
}
