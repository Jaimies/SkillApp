package com.maxpoliakov.skillapp.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails
import kotlinx.coroutines.flow.StateFlow

interface BillingRepository {
    val billingClient: BillingClient
    val isSubscribed: StateFlow<Boolean>

    suspend fun connect()
    suspend fun getSkuDetails(): List<SkuDetails>
}
