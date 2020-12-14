package com.maxpoliakov.skillapp.util.ads

import com.google.android.gms.ads.InterstitialAd
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

    "only shows the ad once if enough time has passed since the last one" {
        val ad = createMockAd(true)
        val adUtil = createAdUtil(ad)
        setClock(clockOfEpochSecond(179))
        adUtil.showAdIfAvailable()
        verify(ad, never()).show()
        setClock(clockOfEpochSecond(180))
        adUtil.showAdIfAvailable()
        verify(ad).show()
    }

    "only show the ad if it's loaded" {
        val ad = createMockAd(false)
        val adUtil = createAdUtil(ad)
        setClock(clockOfEpochSecond(180))
        adUtil.showAdIfAvailable()
        verify(ad, never()).show()
        `when`(ad.isLoaded).thenReturn(true)
        adUtil.showAdIfAvailable()
        verify(ad).show()
    }

    "doesn't request new ads until the existing ones are shown" {
        val adProvider = createMockAdProvider()
        val adUtil = createAdUtil(adProvider = adProvider)
        adUtil.showAdIfAvailable()
        adUtil.showAdIfAvailable()
        verify(adProvider, times(1)).createAd(any())
    }
})

private fun createMockAd(isAdLoaded: Boolean): InterstitialAd {
    val ad = mock(InterstitialAd::class.java)
    `when`(ad.isLoaded).thenReturn(isAdLoaded)
    return ad
}

private fun createMockAdProvider(ad: InterstitialAd = createMockAd(true)): AdProvider {
    val adProvider = mock(AdProvider::class.java)
    `when`(adProvider.createAd(any())).thenReturn(ad)
    return adProvider
}

private fun createAdUtil(
    ad: InterstitialAd = createMockAd(true),
    adProvider: AdProvider = createMockAdProvider(ad)
): AdUtilImpl {
    return AdUtilImpl(adProvider, StubRemoteConfigRepository())
}
