package com.maxpoliakov.skillapp.util.subscriptions

import android.app.Activity
import com.android.billingclient.api.BillingFlowParams
import com.maxpoliakov.skillapp.billing.BillingRepository

suspend fun BillingRepository.showSubscriptionPrompt(activity: Activity) {
    val skuDetails = getSkuDetails()

    if (skuDetails.isEmpty()) return

    val flowParams = BillingFlowParams.newBuilder()
        .setSkuDetails(skuDetails[0])
        .build()

    billingClient.launchBillingFlow(activity, flowParams)
}
