package com.maxpoliakov.skillapp.util.ads

import androidx.navigation.NavDestination
import com.google.android.gms.ads.InterstitialAd
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.clockOfEpochSecond
import com.maxpoliakov.skillapp.domain.repository.RemoteConfigRepository
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.test.any
import io.kotest.core.spec.style.StringSpec
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.time.Duration

class StubRemoteConfigRepository : RemoteConfigRepository {
    override fun getAdInterval(): Duration = Duration.ofMinutes(3)
}

class AdUtilImplTest : StringSpec({
    beforeEach { setClock(clockOfEpochSecond(0)) }

    val navDestination = createNavDestination(R.id.skill_detail_fragment_dest)

    "only shows the ad once if enough time has passed since the last one" {
        val ad = createMockAd(true)
        val adUtil = createAdUtil(ad)
        setClock(clockOfEpochSecond(179))
        adUtil.showAdIfAvailable(navDestination)
        verify(ad, never()).show()
        setClock(clockOfEpochSecond(180))
        adUtil.showAdIfAvailable(navDestination)
        verify(ad).show()
    }

    "only show the ad if it's loaded" {
        val ad = createMockAd(false)
        val adUtil = createAdUtil(ad)
        setClock(clockOfEpochSecond(180))
        adUtil.showAdIfAvailable(navDestination)
        verify(ad, never()).show()
        `when`(ad.isLoaded).thenReturn(true)
        adUtil.showAdIfAvailable(navDestination)
        verify(ad).show()
    }

    "doesn't request new ads until the existing ones are shown" {
        val adProvider = createMockAdProvider()
        val adUtil = createAdUtil(adProvider = adProvider)
        adUtil.showAdIfAvailable(navDestination)
        adUtil.showAdIfAvailable(navDestination)
        verify(adProvider, times(1)).createAd(any())
    }

    "doesn't show the ad if the destination is one of settings, add skill, or edit skill fragments" {
        val ad = createMockAd()
        val adUtil = createAdUtil(ad)
        setClock(clockOfEpochSecond(180))
        adUtil.showAdIfAvailable(createNavDestination(R.id.settings_fragment_dest))
        adUtil.showAdIfAvailable(createNavDestination(R.id.addskill_fragment_dest))
        adUtil.showAdIfAvailable(createNavDestination(R.id.editskill_fragment_dest))
        verify(ad, never()).show()
    }
})

private fun createMockAd(isAdLoaded: Boolean = true): InterstitialAd {
    val ad = mock(InterstitialAd::class.java)
    `when`(ad.isLoaded).thenReturn(isAdLoaded)
    return ad
}

private fun createMockAdProvider(ad: InterstitialAd = createMockAd()): AdProvider {
    val adProvider = mock(AdProvider::class.java)
    `when`(adProvider.createAd(any())).thenReturn(ad)
    return adProvider
}

private fun createNavDestination(id: Int): NavDestination {
    val navDestination = mock(NavDestination::class.java)
    `when`(navDestination.id).thenReturn(id)
    return navDestination
}

private fun createAdUtil(
    ad: InterstitialAd = createMockAd(),
    adProvider: AdProvider = createMockAdProvider(ad)
): AdUtilImpl {
    return AdUtilImpl(adProvider, StubRemoteConfigRepository())
}
