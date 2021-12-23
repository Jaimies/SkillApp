package com.maxpoliakov.skillapp.ui.premium

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.SubscriptionState
import com.maxpoliakov.skillapp.domain.repository.PremiumUtil
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val billingRepository: ExtendedBillingRepository,
    private val premiumUtil: PremiumUtil,
    private val contextProvider: Provider<Context>,
) : ViewModel() {
    private val _showSubscriptionPrompt = SingleLiveEvent<Nothing>()
    val showSubscriptionPrompt: LiveData<Nothing> get() = _showSubscriptionPrompt

    val isSubscribed = billingRepository.subscriptionState.map { state ->
        state == SubscriptionState.Subscribed
    }.asLiveData()

    val hasFreeSubscription = billingRepository.subscriptionState.map { state ->
        state == SubscriptionState.HasFreeSubscription
    }.asLiveData()

    private val _goToManageSubscriptions = SingleLiveEvent<Nothing>()
    val goToManageSubscriptions: LiveData<Nothing> get() = _goToManageSubscriptions

    private val _showError = SingleLiveEvent<Nothing>()
    val showError: LiveData<Nothing> get() = _showError

    val subscriptionExpiryTime = billingRepository.subscriptionState.map { state ->
        if (state != SubscriptionState.Subscribed) return@map null

        return@map try {
            billingRepository.getSubscriptionExpirationTime()
                ?.plusMonths(1)
                ?.format(subscriptionDateFormatter)
        } catch (e: Exception) {
            _showError.call()
            null
        }
    }.asLiveData()

    val freeSubscriptionExpiryDate = billingRepository.subscriptionState.map { state ->
        if (state != SubscriptionState.HasFreeSubscription) return@map null

        val durationTillExpiry = Duration.between(LocalDateTime.now(), premiumUtil.getFreePremiumExpiryDate())

        val hours = durationTillExpiry.toHours()
        val minutes = durationTillExpiry.toMinutesPartCompat()

        if (hours == 0L) return@map contextProvider.get().getString(R.string.mins, minutes);
        return@map contextProvider.get().getString(R.string.hours_and_minutes, hours, minutes)
    }.asLiveData()

    fun showSubscriptionPrompt() = _showSubscriptionPrompt.call()
    fun goToManageSubscriptions() = _goToManageSubscriptions.call()

    companion object {
        private val subscriptionDateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }
}
