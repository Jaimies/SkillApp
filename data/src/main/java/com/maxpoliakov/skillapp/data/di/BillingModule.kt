package com.maxpoliakov.skillapp.data.di

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.maxpoliakov.skillapp.data.billing.PurchaseUpdateHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BillingModule {
    @Provides
    @Singleton
    fun provideBillingClient(
        @ApplicationContext context: Context,
        purchaseUpdateHelper: PurchaseUpdateHelper,
    ): BillingClient {
        return BillingClient.newBuilder(context)
            .setListener(purchaseUpdateHelper)
            .enablePendingPurchases()
            .build()
    }
}
