package com.maxpoliakov.skillapp.util.ads

import com.google.android.gms.ads.InterstitialAd
import com.maxpoliakov.skillapp.domain.repository.RemoteConfigRepository
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.clockOfEpochSecond
import io.kotest.core.spec.style.StringSpec
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.time.Duration

class StubRemoteConfigRepository : RemoteConfigRepository {
    override fun getAdInterval(): Duration = Duration.ofMinutes(3)
}

class StubAdProvider(private val ad: InterstitialAd) : AdProvider {
    override fun createAd(onClosed: () -> Unit) = ad
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
})

private fun createMockAd(isAdLoaded: Boolean): InterstitialAd {
    val ad = Mockito.mock(InterstitialAd::class.java)
    `when`(ad.isLoaded).thenReturn(isAdLoaded)
    return ad
}

private fun createAdUtil(ad: InterstitialAd) : AdUtilImpl {
    return AdUtilImpl(StubAdProvider(ad), StubRemoteConfigRepository())
}
