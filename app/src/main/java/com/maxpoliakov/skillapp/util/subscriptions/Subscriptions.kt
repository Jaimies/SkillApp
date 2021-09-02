package com.maxpoliakov.skillapp.util.subscriptions

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.maxpoliakov.skillapp.billing.BillingRepository

suspend fun BillingClient.showSubscriptionPrompt(billingRepository: BillingRepository, activity: Activity) {
    val skuDetails = billingRepository.getSubscriptionSkuDetails() ?: return

    val flowParams = BillingFlowParams.newBuilder()
        .setSkuDetails(skuDetails)
        .build()

    launchBillingFlow(activity, flowParams)
}
