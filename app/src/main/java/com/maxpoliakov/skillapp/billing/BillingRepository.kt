package com.maxpoliakov.skillapp.billing

import com.android.billingclient.api.SkuDetails
import kotlinx.coroutines.flow.StateFlow

interface BillingRepository {
    val isSubscribed: StateFlow<Boolean>

    suspend fun connect()
    suspend fun getSkuDetails(): List<SkuDetails>
}
