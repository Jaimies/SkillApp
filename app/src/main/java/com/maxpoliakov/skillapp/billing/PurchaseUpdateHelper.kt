package com.maxpoliakov.skillapp.billing

import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchaseUpdateHelper @Inject constructor() : PurchasesUpdatedListener {
    private val listeners = mutableListOf<PurchasesUpdatedListener>()

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        listeners.forEach { listener ->
            listener.onPurchasesUpdated(billingResult, purchases)
        }
    }

    fun addListener(listener: PurchasesUpdatedListener) {
        listeners.add(listener)
    }
}
