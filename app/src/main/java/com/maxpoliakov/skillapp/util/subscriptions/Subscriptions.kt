package com.maxpoliakov.skillapp.util.subscriptions

import android.app.Activity
import android.view.View
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.billing.ExtendedBillingRepository
import com.maxpoliakov.skillapp.util.dialog.showSnackbar

suspend fun BillingClient.showSubscriptionPrompt(
    billingRepository: ExtendedBillingRepository,
    activity: Activity,
    view: View,
) {
    val skuDetails = try {
        billingRepository.getSubscriptionSkuDetails() ?: return
    } catch (e: Exception) {
        view.showSnackbar(R.string.something_went_wrong)
        return
    }

    val flowParams = BillingFlowParams.newBuilder()
        .setSkuDetails(skuDetails)
        .build()

    launchBillingFlow(activity, flowParams)
}
