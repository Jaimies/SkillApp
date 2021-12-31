package com.maxpoliakov.skillapp.data.billing

import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.Purchase.PurchaseState.PURCHASED
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.maxpoliakov.skillapp.data.logToCrashlytics
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.Companion.SUBSCRIPTION_SKU_NAME
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.ConnectionState
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.SubscriptionState
import com.maxpoliakov.skillapp.domain.repository.PremiumUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val premiumUtil: PremiumUtil,
    private val ioScope: CoroutineScope,
) : ExtendedBillingRepository {

    private val listeners = mutableListOf<() -> Unit>()

    private val _subscriptionState = MutableStateFlow(SubscriptionState.Loading)
    override val subscriptionState get() = _subscriptionState.asStateFlow()

    override var connectionState = ConnectionState.NotStarted
        private set

    override fun notifyPremiumGranted() {
        _subscriptionState.value = SubscriptionState.HasFreeSubscription
    }

    override suspend fun connect() = coroutineScope {
        if (connectionState == ConnectionState.Connected) return@coroutineScope

        if (connectionState == ConnectionState.Started) {
            awaitPlayServicesReady()
            return@coroutineScope
        }

        connectionState = ConnectionState.Started
        if (premiumUtil.isFreePremiumAvailable()) {
            _subscriptionState.value = SubscriptionState.HasFreeSubscription
            return@coroutineScope
        }

        connectToPlay()

        purchaseUpdateHelper.addListener(PurchasesUpdatedListener { _, purchases ->
            val validPurchases = (purchases ?: return@PurchasesUpdatedListener)

            ioScope.launch {
                updateSubscriptionState(validPurchases)
                acknowledgePurchaseIfNeeded(purchases)
            }
        })

        val purchasesResult = billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS)
        val purchases = purchasesResult.purchasesList

        acknowledgePurchaseIfNeeded(purchases)
        updateSubscriptionState(purchases)
    }

    private suspend fun acknowledgePurchaseIfNeeded(purchases: List<Purchase>) {
        val subscriptionPurchase = purchases.find { purchase ->
            purchase.purchaseState == PURCHASED
                    && !purchase.isAcknowledged
                    && purchase.skus.contains("premium_subscription")
        } ?: return

        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(subscriptionPurchase.purchaseToken)
            .build()

        billingClient.acknowledgePurchase(params)
    }

    private suspend fun updateSubscriptionState(purchases: List<Purchase>) {
        val freePremiumAvailable = premiumUtil.isFreePremiumAvailable()
        val isSubscribed = isSubscribed(purchases)
        _subscriptionState.emit(getSubscriptionState(isSubscribed, freePremiumAvailable))
    }

    private fun getSubscriptionState(isSubscribed: Boolean, hasFreePremium: Boolean): SubscriptionState {
        return when {
            isSubscribed -> SubscriptionState.Subscribed
            hasFreePremium -> SubscriptionState.HasFreeSubscription
            else -> SubscriptionState.NotSubscribed
        }
    }

    private suspend fun connectToPlay() {
        return suspendCoroutine { continuation ->
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        continuation.resume(Unit)
                        listeners.forEach { it.invoke() }
                        listeners.clear()
                        connectionState = ConnectionState.Connected
                    } else {
                        handleFailedConnection(billingResult)
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
    }

    private fun handleFailedConnection(billingResult: BillingResult) {
        connectionState = if (billingResult.responseCode == BillingResponseCode.BILLING_UNAVAILABLE)
            ConnectionState.BillingUnavailable else
            ConnectionState.FailedToConnect

        _subscriptionState.value = SubscriptionState.FailedToLoad

        Exception(
            """Failed to connect to Google Play Billing, 
                                    |response code: ${billingResult.responseCode},
                                    |message: ${billingResult.debugMessage}""".trimMargin()
        ).logToCrashlytics()
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
}
