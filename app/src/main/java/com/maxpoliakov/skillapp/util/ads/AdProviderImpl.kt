package com.maxpoliakov.skillapp.util.ads

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val INTERSTITIAL_AD_ID = "ca-app-pub-3620260144623649/3189554368"

class AdProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AdProvider {

    override fun createAd(onClosed: () -> Unit): InterstitialAd {
        val ad = InterstitialAd(context)
        ad.adUnitId = INTERSTITIAL_AD_ID
        ad.loadAd(AdRequest.Builder().build())
        ad.adListener = object : AdListener() {
            override fun onAdClosed() = onClosed()
        }
        return ad
    }
}
