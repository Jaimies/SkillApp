package com.maxpoliakov.skillapp.data.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState.PURCHASED
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.queryPurchasesAsync
import com.android.billingclient.api.querySkuDetails
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.maxpoliakov.skillapp.data.logToCrashlytics
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.Companion.SUBSCRIPTION_SKU_NAME
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.SubscriptionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class BillingRepositoryImpl @Inject constructor(
    private val billingClient: BillingClient,
    private val purchaseUpdateHelper: PurchaseUpdateHelper,
    private val ioScope: CoroutineScope,
) : ExtendedBillingRepository {

    private val listeners = mutableListOf<() -> Unit>()

    private val _isSubscribed = MutableStateFlow(false)
    override val isSubscribed = _isSubscribed.asStateFlow()

    private val _subscriptionState = MutableStateFlow(SubscriptionState.Loading)
    override val subscriptionState get() = _subscriptionState.asStateFlow()

    private var connectionState = ConnectionState.NotStarted

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

            ioScope.launch {
                updateSubscriptionState(validPurchases)
            }
        })

        val purchasesResult = billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS)
        val purchases = purchasesResult.purchasesList

        updateSubscriptionState(purchases)
    }

    private suspend fun updateSubscriptionState(purchases: List<Purchase>) {
        val isSubscribed = isSubscribed(purchases)
        _isSubscribed.emit(isSubscribed)
        _subscriptionState.emit(getSubscriptionState(isSubscribed))
    }

    private fun getSubscriptionState(isSubscribed: Boolean): SubscriptionState {
        return if (isSubscribed) SubscriptionState.Subscribed
        else SubscriptionState.NotSubscribed
    }

    private suspend fun connectToPlay() {
        return suspendCoroutine { continuation ->
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        continuation.resume(Unit)
                        listeners.forEach { it.invoke() }
                        listeners.clear()
                    } else {
                        Exception(
                            """Failed to connect to Google Play Billing, 
                                |response code: ${billingResult.responseCode},
                                |message: ${billingResult.debugMessage}""".trimMargin()
                        ).logToCrashlytics()
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
    }

    override fun addOneTimePurchaseUpdateListener(listener: PurchasesUpdatedListener) {
        purchaseUpdateHelper.addOneTimeListener(listener)
    }

    override suspend fun getSubscriptionSkuDetails(): SkuDetails? = withContext(Dispatchers.IO) {
        if (!billingClient.isReady) throwClientNotReadyException()

        val skuList = listOf(SUBSCRIPTION_SKU_NAME)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

        val skuDetailsResult = billingClient.querySkuDetails(params.build())
        skuDetailsResult.skuDetailsList?.takeIf { it.isNotEmpty() }?.let { it[0] }
    }

    override suspend fun getSubscriptionExpirationTime() = withContext(Dispatchers.IO) {
        if (!billingClient.isReady) throwClientNotReadyException()

        val purchasesResult = billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS)
        val purchases = purchasesResult.purchasesList

        val purchase = purchases.firstOrNull { it.skus.contains(SUBSCRIPTION_SKU_NAME) }
            ?: return@withContext null

        val instant = Instant.ofEpochMilli(purchase.purchaseTime)
        instant?.let { LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) }
    }

    private fun throwClientNotReadyException(): Nothing {
        val e = IllegalStateException("Billing client not ready")
        FirebaseCrashlytics.getInstance().recordException(e)
        e.printStackTrace()
        throw e
    }

    private suspend fun awaitPlayServicesReady() {
        return suspendCoroutine { continuation ->
            listeners.add { continuation.resume(Unit) }
        }
    }

    companion object {
        fun isSubscribed(purchases: List<Purchase>): Boolean {
            return purchases.any { purchase ->
                purchase.purchaseState == PURCHASED && purchase.skus.contains(SUBSCRIPTION_SKU_NAME)
            }
        }
    }

    enum class ConnectionState {
        NotStarted, Started, Connected
    }
}
