package com.maxpoliakov.skillapp.util.subscriptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SubscriptionUIUtil @Inject constructor(
    private val billingRepository: ExtendedBillingRepository,
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

    companion object {
        private val subscriptionDateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }
}
