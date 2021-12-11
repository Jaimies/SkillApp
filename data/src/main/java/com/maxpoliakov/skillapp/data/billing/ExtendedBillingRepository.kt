package com.maxpoliakov.skillapp.data.billing

import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.maxpoliakov.skillapp.domain.repository.BillingRepository

interface ExtendedBillingRepository : BillingRepository {
    fun addOneTimePurchaseUpdateListener(listener: PurchasesUpdatedListener)
    suspend fun getSubscriptionSkuDetails(): SkuDetails?
}
