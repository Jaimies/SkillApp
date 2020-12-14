package com.maxpoliakov.skillapp.util.ads

import com.google.android.gms.ads.InterstitialAd
import com.maxpoliakov.skillapp.domain.repository.RemoteConfigRepository
import com.maxpoliakov.skillapp.shared.util.getCurrentDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.SECONDS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdUtilImpl @Inject constructor(
    private val adProvider: AdProvider,
    private val remoteConfigRepository: RemoteConfigRepository
) : AdUtil {

    private var ad: InterstitialAd? = null
    private var lastAdTime = getCurrentDateTime()

    override fun showAdIfAvailable() {
        val ad = this.ad
        if (ad != null && canShowAd(ad)) ad.show()
        this.ad = createAd()
    }

    private fun canShowAd(ad: InterstitialAd): Boolean {
        val seconds = lastAdTime.until(getCurrentDateTime(), SECONDS)
        return ad.isLoaded && seconds >= remoteConfigRepository.getAdInterval().seconds
    }

    private fun createAd(): InterstitialAd {
        return adProvider.createAd {
            lastAdTime = getCurrentDateTime()
        }
    }
}
