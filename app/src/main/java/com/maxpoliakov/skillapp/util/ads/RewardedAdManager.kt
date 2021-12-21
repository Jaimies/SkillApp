package com.maxpoliakov.skillapp.util.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdManager {
    private var mRewardedAd: RewardedAd? = null

    fun load(context: Context) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            admobRewardedUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {}

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                }
            })
    }

    fun show(activity: Activity, onUserEarnedRewardListener: (RewardItem) -> Unit) {
        mRewardedAd?.show(activity, onUserEarnedRewardListener)
        mRewardedAd = null
    }

    companion object {
        private const val admobRewardedUnitId = "ca-app-pub-3940256099942544/5224354917"
    }
}
