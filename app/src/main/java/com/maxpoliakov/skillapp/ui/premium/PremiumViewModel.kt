package com.maxpoliakov.skillapp.ui.premium

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.SkuDetails
import com.maxpoliakov.skillapp.billing.BillingRepository
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    billingRepository: BillingRepository
) : ViewModel() {
    private val _showSubscriptionPrompt = SingleLiveEvent<Nothing>()
    val showSubscriptionPrompt: LiveData<Nothing> get() = _showSubscriptionPrompt

    val isSubscribed = billingRepository.isSubscribed.asLiveData()
    private val _skuDetails = MutableLiveData<SkuDetails?>()
    val skuDetails: LiveData<SkuDetails?> get() = _skuDetails

    private val _subscriptionExpiryTime = MutableLiveData<String>()
    val subscriptionExpiryTime: LiveData<String> get() = _subscriptionExpiryTime

    init {
        viewModelScope.launch {
            _skuDetails.value = billingRepository.getSubscriptionSkuDetails()
        }

        viewModelScope.launch {
            val date = billingRepository.getSubscriptionExpirationTime()
            _subscriptionExpiryTime.value = date
                ?.plusMonths(1)
                ?.format(subscriptionDateFormatter)
        }
    }

    fun showSubscriptionPrompt() = _showSubscriptionPrompt.call()

    companion object {
        private val subscriptionDateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }
}
