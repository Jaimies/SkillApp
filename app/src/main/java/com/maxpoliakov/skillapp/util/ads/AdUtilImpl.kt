package com.maxpoliakov.skillapp.util.ads

import com.google.android.gms.ads.InterstitialAd
import com.maxpoliakov.skillapp.shared.util.getCurrentDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.SECONDS
import javax.inject.Inject
import javax.inject.Singleton

const val AD_INTERVAL = 180

@Singleton
class AdUtilImpl @Inject constructor(
    private val adProvider: AdProvider
) : AdUtil {

    private var ad: InterstitialAd? = null
    private var lastAdTime = LocalDateTime.now()

    override fun showAdIfAvailable() {
        val ad = this.ad
        if (ad != null && canShowAd(ad)) ad.show()
        this.ad = createAd()
    }

    private fun canShowAd(ad: InterstitialAd): Boolean {
        val seconds = lastAdTime.until(getCurrentDateTime(), SECONDS)
        return ad.isLoaded && seconds >= AD_INTERVAL
    }

    private fun createAd(): InterstitialAd {
        return adProvider.createAd {
            lastAdTime = getCurrentDateTime()
        }
    }
}
