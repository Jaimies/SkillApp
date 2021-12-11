package com.maxpoliakov.skillapp.data.billing

import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchaseUpdateHelper @Inject constructor() : PurchasesUpdatedListener {
    private val listeners = mutableListOf<PurchasesUpdatedListener>()
    private val oneTimeListeners = mutableListOf<PurchasesUpdatedListener>()

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        listeners.forEach { listener ->
            listener.onPurchasesUpdated(billingResult, purchases)
        }
        oneTimeListeners.forEach { listener ->
            listener.onPurchasesUpdated(billingResult, purchases)
        }
        oneTimeListeners.clear()
    }

    fun addListener(listener: PurchasesUpdatedListener) {
        listeners.add(listener)
    }

    fun addOneTimeListener(listener: PurchasesUpdatedListener) {
        oneTimeListeners.add(listener)
    }
}
