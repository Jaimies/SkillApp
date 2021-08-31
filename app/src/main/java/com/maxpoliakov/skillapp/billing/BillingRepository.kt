package com.maxpoliakov.skillapp.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails

interface BillingRepository {
    val billingClient: BillingClient
    val isSubscribed: Boolean

    suspend fun connect()
    suspend fun getSkuDetails(): List<SkuDetails>
}
