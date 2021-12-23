package com.maxpoliakov.skillapp.util.subscriptions

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.domain.repository.PremiumUtil
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Provider

class SubscriptionUIUtil @Inject constructor(
    private val billingRepository: ExtendedBillingRepository,
    private val premiumUtil: PremiumUtil,
    @ApplicationContext
    private val contextProvider: Provider<Context>,
) {
    private val _onError = SingleLiveEvent<Nothing>()
    val onError: LiveData<Nothing> get() = _onError

    val subscriptionExpiryTime = billingRepository.subscriptionState.map { state ->
        if (state != BillingRepository.SubscriptionState.Subscribed) return@map null

        return@map try {
            billingRepository.getSubscriptionExpirationTime()
                ?.plusMonths(1)
                ?.format(subscriptionDateFormatter)
        } catch (e: Exception) {
            _onError.call()
            null
        }
    }.asLiveData()

    val freeSubscriptionExpiryDate = billingRepository.subscriptionState.map { state ->
        if (state != BillingRepository.SubscriptionState.HasFreeSubscription) return@map null

        val durationTillExpiry = Duration.between(LocalDateTime.now(), premiumUtil.getFreePremiumExpiryDate())

        val hours = durationTillExpiry.toHours()
        val minutes = durationTillExpiry.toMinutesPartCompat()

        if (hours == 0L) return@map contextProvider.get().getString(R.string.mins, minutes);
        return@map contextProvider.get().getString(R.string.hours_and_minutes, hours, minutes)
    }.asLiveData()

    companion object {
        private val subscriptionDateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }
}
