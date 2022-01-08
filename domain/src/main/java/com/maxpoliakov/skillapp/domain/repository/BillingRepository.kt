package com.maxpoliakov.skillapp.domain.repository

import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface BillingRepository {
    val subscriptionState: StateFlow<SubscriptionState>
    val connectionState: ConnectionState

    suspend fun connect()
    suspend fun getSubscriptionExpirationTime(): LocalDateTime?

    enum class SubscriptionState(val hasAccessToPremium: Boolean) {
        Loading(false), FailedToLoad(false), Subscribed(true), NotSubscribed(false)
    }

    enum class ConnectionState {
        NotStarted, Started, Connected, BillingUnavailable, FailedToConnect
    }

    companion object {
        const val SUBSCRIPTION_SKU_NAME = "premium_subscription"
    }
}
