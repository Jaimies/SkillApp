package com.maxpoliakov.skillapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.ads.AdConsentUtilImpl
import com.maxpoliakov.skillapp.data.ads.AdConsentUtilImpl.ConsentState
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.domain.repository.PremiumUtil
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.ui.premium.PremiumIntro
import com.maxpoliakov.skillapp.util.ads.RewardedAdManager
import com.maxpoliakov.skillapp.util.ads.RewardedAdManager.LoadingState
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

    @Inject
    lateinit var premiumUtil: PremiumUtil

    @Inject
    lateinit var adConsentUtil: AdConsentUtilImpl

    private val adManager = RewardedAdManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (adConsentUtil.consentState == ConsentState.CanShowAds)
            adManager.loadAd(requireContext())
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

        if (!billingRepository.subscriptionState.value.hasAccessToPremium)
            backupsPref.widgetLayoutResource = R.layout.premium_widget

        lifecycleScope.launch {
            billingRepository.subscriptionState.collect { state ->
                backupsPref.widgetLayoutResource =
                    if (state.hasAccessToPremium) R.layout.empty_widget
                    else R.layout.premium_widget
            }
        }

        backupsPref.setOnPreferenceClickListener {
            val controller = findNavController()

            val destination = if (billingRepository.subscriptionState.value.hasAccessToPremium)
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

        val adPref = findPreference<Preference>("premium_ad")!!


        adPref.setOnPreferenceClickListener {
            adConsentUtil.showConsentFormIfNecessary(requireActivity()) { formError ->
                adManager.loadAd(requireContext())
                adManager.setOnAdLoadedListener(this::tryToShowAd)
            }

            if (adConsentUtil.consentState == ConsentState.MustRequestConsent) {
                return@setOnPreferenceClickListener true
            }

            tryToShowAd(adManager.loadingState)

            true
        }
    }

    private fun tryToShowAd(loadingState: LoadingState) {
        if (loadingState == LoadingState.FailedToLoad) {
            showAdFailedToLoadSnackBar()
            return
        }

        if (loadingState == LoadingState.Loading) {
            showAdIsLoadingSnackbar()

            adManager.setOnAdLoadedListener { state ->
                if (state == LoadingState.Loaded) showTheAd()
                else showAdFailedToLoadSnackBar()
            }

            return
        }

        showTheAd()
    }

    private fun showAdIsLoadingSnackbar() {
        Snackbar
            .make(requireView(), "The ad is loading, it will show as soon as it finishes loading", Snackbar.LENGTH_LONG)
            .show()
    }

    private fun showAdFailedToLoadSnackBar() {
        Snackbar
            .make(requireView(), "Ad failed to load, please try again later", Snackbar.LENGTH_LONG)
            .show()
    }

    private fun showTheAd() {
        adManager.showAdIfAvailable(requireActivity()) {
            billingRepository.notifyPremiumGranted()
            premiumUtil.enableFreePremium()
            PremiumIntro.showIfNeeded(requireActivity())
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
