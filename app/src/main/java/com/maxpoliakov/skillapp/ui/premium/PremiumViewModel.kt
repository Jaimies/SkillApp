package com.maxpoliakov.skillapp.ui.premium

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.SubscriptionState
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.subscriptions.SubscriptionUIUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    billingRepository: ExtendedBillingRepository,
    private val subscriptionUIUtil: SubscriptionUIUtil,
) : ViewModel() {
    private val _priceFailedToLoad = MutableLiveData(false)
    val priceFailedToLoad: LiveData<Boolean> get() = _priceFailedToLoad

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

    val skuDetails = billingRepository.subscriptionState.map { state ->
        _priceFailedToLoad.value = false

        if (state.hasAccessToPremium) return@map null

        return@map try {
            val skuDetails = billingRepository.getSubscriptionSkuDetails() ?: return@map null
            skuDetails.price
        } catch (e: Exception) {
            _priceFailedToLoad.postValue(true)
            null
        }
    }.asLiveData()

    val showError get() = subscriptionUIUtil.onError
    val subscriptionExpiryTime get() = subscriptionUIUtil.subscriptionExpiryTime
    val freeSubscriptionExpiryDate get() = subscriptionUIUtil.freeSubscriptionExpiryDate

    fun showSubscriptionPrompt() = _showSubscriptionPrompt.call()
    fun goToManageSubscriptions() = _goToManageSubscriptions.call()
}
