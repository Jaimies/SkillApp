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

    init {
        viewModelScope.launch {
            _skuDetails.value = billingRepository.getSubscriptionSkuDetails()
        }
    }

    fun showSubscriptionPrompt() = _showSubscriptionPrompt.call()
}
