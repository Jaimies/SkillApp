package com.maxpoliakov.skillapp.screenshots

import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import javax.inject.Inject

class StubBillingRepository @Inject constructor() : ExtendedBillingRepository {
    override fun addOneTimePurchaseUpdateListener(listener: PurchasesUpdatedListener) {}

    override suspend fun getSubscriptionSkuDetails(): SkuDetails? {
        return null
    }

    override val subscriptionState: StateFlow<BillingRepository.SubscriptionState>
        get() = MutableStateFlow(BillingRepository.SubscriptionState.Subscribed)

    override val connectionState: BillingRepository.ConnectionState
        get() = BillingRepository.ConnectionState.Connected

    override suspend fun connect() {}

    override suspend fun getSubscriptionExpirationTime(): LocalDateTime? {
        return LocalDateTime.now().plusMonths(1)
    }
}
