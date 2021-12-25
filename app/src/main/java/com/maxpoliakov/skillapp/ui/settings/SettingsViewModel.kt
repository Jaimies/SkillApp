package com.maxpoliakov.skillapp.ui.settings

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.ads.AdConsentUtilImpl
import com.maxpoliakov.skillapp.data.ads.AdConsentUtilImpl.ConsentState
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.domain.repository.PremiumUtil
import com.maxpoliakov.skillapp.domain.usecase.backup.CreateBackupUseCase
import com.maxpoliakov.skillapp.ui.premium.PremiumIntro
import com.maxpoliakov.skillapp.util.ads.RewardedAdManager
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.subscriptions.SubscriptionUIUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val billingRepository: BillingRepository,
    private val premiumUtil: PremiumUtil,
    private val adConsentUtil: AdConsentUtilImpl,
    private val subscriptionUIUtil: SubscriptionUIUtil,
    private val createBackupUseCase: CreateBackupUseCase,
    private val ioScope: CoroutineScope,
) : ViewModel() {
    private val adManager = RewardedAdManager()

    val timeTillFreeSubscriptionExpires get() = subscriptionUIUtil.freeSubscriptionExpiryDate

    private val _showSnackbar = SingleLiveEvent<Int>()
    val showSnackbar: LiveData<Int> get() = _showSnackbar

    val subscriptionState get() = billingRepository.subscriptionState

    fun loadAdIfPossible(context: Context) {
        if (adConsentUtil.consentState == ConsentState.CanShowAds)
            adManager.loadAd(context)
    }

    fun onWatchAdClicked(activity: Activity) {
        if (billingRepository.subscriptionState.value.hasAccessToPremium) return

        adConsentUtil.showConsentFormIfNecessary(activity) { formError ->
            adManager.loadAd(activity)
            showAdIsLoadingSnackbar()
            adManager.setOnAdLoadedListener { loadingState ->
                tryToShowAd(loadingState, activity)
            }
        }

        if (adConsentUtil.consentState == ConsentState.MustRequestConsent) return
        tryToShowAd(adManager.loadingState, activity)
    }

    private fun tryToShowAd(loadingState: RewardedAdManager.LoadingState, activity: Activity) {
        if (loadingState == RewardedAdManager.LoadingState.FailedToLoad) {
            showAdFailedToLoadSnackBar()
            return
        }

        if (loadingState == RewardedAdManager.LoadingState.Loading) {
            showAdIsLoadingSnackbar()

            adManager.setOnAdLoadedListener { state ->
                if (state == RewardedAdManager.LoadingState.Loaded) showTheAd(activity)
                else showAdFailedToLoadSnackBar()
            }

            return
        }

        showTheAd(activity)
    }

    private fun showTheAd(activity: Activity) {
        adManager.showAdIfAvailable(activity) {
            premiumUtil.enableFreePremium()
            billingRepository.notifyPremiumGranted()
            PremiumIntro.showIfNeeded(activity)
            ioScope.launch {
                createBackupUseCase.createBackup()
            }
        }
    }

    private fun showAdIsLoadingSnackbar() {
        _showSnackbar.value = R.string.ad_loading
    }

    private fun showAdFailedToLoadSnackBar() {
        _showSnackbar.value = R.string.ad_failed_to_load
    }
}
