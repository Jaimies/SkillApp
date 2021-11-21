package com.maxpoliakov.skillapp.domain.repository

import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface BillingRepository {
    val isSubscribed: StateFlow<Boolean>
    val subscriptionState: StateFlow<SubscriptionState>

    suspend fun connect()
    suspend fun getSubscriptionExpirationTime(): LocalDateTime?

    enum class SubscriptionState {
        Loading, Subscribed, NotSubscribed,
    }

    companion object {
        const val SUBSCRIPTION_SKU_NAME = "premium_subscription"
    }
}
