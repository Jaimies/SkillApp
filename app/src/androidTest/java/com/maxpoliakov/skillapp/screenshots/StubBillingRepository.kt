package com.maxpoliakov.skillapp.screenshots

import com.android.billingclient.api.SkuDetails
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import javax.inject.Inject

class StubBillingRepository @Inject constructor() : ExtendedBillingRepository {
    override suspend fun getSubscriptionSkuDetails(): SkuDetails? {
        return null
    }

    override val isSubscribed: StateFlow<Boolean>
        get() = MutableStateFlow(true)

    override suspend fun connect() {}

    override suspend fun getSubscriptionExpirationTime(): LocalDateTime? {
        return LocalDateTime.now().plusMonths(1)
    }
}
