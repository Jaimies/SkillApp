package com.maxpoliakov.skillapp.ui.premium

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.billing.BillingRepository
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    billingRepository: BillingRepository
) : ViewModel() {
    private val _showSubscriptionPrompt = SingleLiveEvent<Nothing>()
    val showSubscriptionPrompt: LiveData<Nothing> get() = _showSubscriptionPrompt

    val isSubscribed = billingRepository.isSubscribed.asLiveData()

    fun showSubscriptionPrompt() = _showSubscriptionPrompt.call()
}
