package com.maxpoliakov.skillapp.util.subscriptions

import android.app.Activity
import android.content.Intent
import android.view.View
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.google.android.material.snackbar.Snackbar
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.billing.BillingRepositoryImpl
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.domain.repository.BillingRepository.ConnectionState
import com.maxpoliakov.skillapp.ui.premium.PremiumIntro
import com.maxpoliakov.skillapp.util.dialog.showSnackbar

suspend fun BillingClient.showSubscriptionPrompt(
    billingRepository: ExtendedBillingRepository,
    activity: Activity,
    view: View,
) {
    if (billingRepository.connectionState == ConnectionState.BillingUnavailable) {
        view.showSnackbar(R.string.billing_unavailable, Snackbar.LENGTH_INDEFINITE)
        return
    }

    val skuDetails = try {
        billingRepository.getSubscriptionSkuDetails() ?: return
    } catch (e: Exception) {
        view.showSnackbar(R.string.something_went_wrong)
        return
    }

    val flowParams = BillingFlowParams.newBuilder()
        .setSkuDetails(skuDetails)
        .build()

    billingRepository.addOneTimePurchaseUpdateListener(PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) return@PurchasesUpdatedListener
        if (BillingRepositoryImpl.isSubscribed(purchases as List<Purchase>)) {
            activity.startActivity(Intent(activity, PremiumIntro::class.java))
        }
    })

    launchBillingFlow(activity, flowParams)
}
