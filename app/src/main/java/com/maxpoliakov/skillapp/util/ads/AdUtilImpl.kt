package com.maxpoliakov.skillapp.util.ads

import androidx.navigation.NavDestination
import com.google.android.gms.ads.InterstitialAd
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.repository.RemoteConfigRepository
import com.maxpoliakov.skillapp.shared.util.getCurrentDateTime
import java.time.temporal.ChronoUnit.SECONDS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdUtilImpl @Inject constructor(
    private val adProvider: AdProvider,
    private val remoteConfigRepository: RemoteConfigRepository
) : AdUtil {

    private var ad = createAd()
    private var lastAdTime = getCurrentDateTime()

    override fun showAdIfAvailable(destination: NavDestination) {
        if (canShowAd(destination)) {
            ad.show()
            ad = createAd()
        }
    }

    private fun canShowAd(destination: NavDestination): Boolean {
        return ad.isLoaded && canShowAdInDestination(destination) && enoughTimePassedSinceLastAd()
    }

    private fun canShowAdInDestination(destination: NavDestination): Boolean {
        return destination.id !in listOf(
            R.id.settings_fragment_dest,
            R.id.addskill_fragment_dest,
            R.id.editskill_fragment_dest
        )
    }

    private fun enoughTimePassedSinceLastAd(): Boolean {
        val seconds = lastAdTime.until(getCurrentDateTime(), SECONDS)
        return seconds >= remoteConfigRepository.getAdInterval().seconds
    }

    private fun createAd(): InterstitialAd {
        return adProvider.createAd {
            lastAdTime = getCurrentDateTime()
        }
    }
}
