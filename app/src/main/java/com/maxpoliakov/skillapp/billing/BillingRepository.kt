package com.maxpoliakov.skillapp.billing

import com.android.billingclient.api.SkuDetails
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface BillingRepository {
    val isSubscribed: StateFlow<Boolean>

    suspend fun connect()
    suspend fun getSubscriptionSkuDetails(): SkuDetails?
    suspend fun getSubscriptionExpirationTime(): LocalDateTime?

    companion object {
        const val SUBSCRIPTION_SKU_NAME = "premium_subscription"
    }
}
