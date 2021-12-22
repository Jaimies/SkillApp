package com.maxpoliakov.skillapp.ui.premium

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val billingRepository: ExtendedBillingRepository,
) : ViewModel() {
    private val _showSubscriptionPrompt = SingleLiveEvent<Nothing>()
    val showSubscriptionPrompt: LiveData<Nothing> get() = _showSubscriptionPrompt

    val isSubscribed = billingRepository.isSubscribed.asLiveData()

    private val _goToManageSubscriptions = SingleLiveEvent<Nothing>()
    val goToManageSubscriptions: LiveData<Nothing> get() = _goToManageSubscriptions

    private val _showError = SingleLiveEvent<Nothing>()
    val showError: LiveData<Nothing> get() = _showError

    val subscriptionExpiryTime = billingRepository.isSubscribed.map { isSubscribed ->
        if (!isSubscribed) return@map null

        return@map try {
            billingRepository.getSubscriptionExpirationTime()
                ?.plusMonths(1)
                ?.format(subscriptionDateFormatter)
        } catch (e: Exception) {
            _showError.call()
            null
        }
    }.asLiveData()

    fun showSubscriptionPrompt() = _showSubscriptionPrompt.call()
    fun goToManageSubscriptions() = _goToManageSubscriptions.call()

    companion object {
        private val subscriptionDateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }
}
