package com.maxpoliakov.skillapp.data.billing

import com.android.billingclient.api.SkuDetails
import com.maxpoliakov.skillapp.domain.repository.BillingRepository

interface ExtendedBillingRepository: BillingRepository {
    suspend fun getSubscriptionSkuDetails(): SkuDetails?
}
