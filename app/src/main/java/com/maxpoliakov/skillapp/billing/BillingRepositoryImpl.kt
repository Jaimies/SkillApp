package com.maxpoliakov.skillapp.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.queryPurchasesAsync
import com.android.billingclient.api.querySkuDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class BillingRepositoryImpl @Inject constructor(
    private val billingClient: BillingClient,
    private val purchaseUpdateHelper: PurchaseUpdateHelper,
    private val ioScope: CoroutineScope,
) : BillingRepository {

    private val listeners = mutableListOf<() -> Unit>()

    private val _isSubscribed = MutableStateFlow(false)
    override val isSubscribed = _isSubscribed.asStateFlow()

    private var connectionState = ConnectionState.NotStarted

    init {
        ioScope.launch {
            awaitPlayServicesReady()
            val purchasesResult = billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS)
            val purchases = purchasesResult.purchasesList
            _isSubscribed.emit(isSubscribed(purchases))
        }
    }

    private fun isSubscribed(purchases: List<Purchase>): Boolean {
        return purchases.any { purchase -> purchase.skus.contains("premium_subscription") }
    }

    override suspend fun connect() = coroutineScope {
        if (connectionState == ConnectionState.Connected) return@coroutineScope

        if (connectionState == ConnectionState.Started) {
            awaitPlayServicesReady()
            return@coroutineScope
        }

        connectionState = ConnectionState.Started
        connectToPlay()
        connectionState = ConnectionState.Connected
        purchaseUpdateHelper.addListener(PurchasesUpdatedListener { _, purchases ->
            val validPurchases = (purchases ?: return@PurchasesUpdatedListener)
                .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }

            ioScope.launch {
                _isSubscribed.emit(isSubscribed(validPurchases))
            }
        })
    }

    private suspend fun connectToPlay() {
        return suspendCoroutine { continuation ->
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        continuation.resume(Unit)
                        listeners.forEach { it.invoke() }
                        listeners.clear()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        }
    }

    override suspend fun getSkuDetails(): List<SkuDetails> = withContext(Dispatchers.IO) {
        val skuList = listOf("premium_subscription")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

        val skuDetailsResult = billingClient.querySkuDetails(params.build())
        skuDetailsResult.skuDetailsList ?: listOf()
    }

    private suspend fun awaitPlayServicesReady() {
        return suspendCoroutine { continuation ->
            listeners.add { continuation.resume(Unit) }
        }
    }

    enum class ConnectionState {
        NotStarted, Started, Connected
    }
}
