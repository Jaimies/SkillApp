package com.maxpoliakov.skillapp.util.ads

import com.google.android.gms.ads.InterstitialAd

interface AdProvider {
    fun createAd(onClosed: () -> Unit = {}): InterstitialAd
}
