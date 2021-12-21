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
    var loadingState = LoadingState.Loading

    fun loadAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            admobRewardedUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    loadingState = LoadingState.FailedToLoad
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                    loadingState = LoadingState.Loaded
                }
            })
    }

    fun showAdIfAvailable(activity: Activity, onUserEarnedRewardListener: (RewardItem) -> Unit) {
        mRewardedAd?.run  {
            show(activity, onUserEarnedRewardListener)
            mRewardedAd = null
        }
    }

    enum class LoadingState {
        Loading, Loaded, FailedToLoad
    }

    companion object {
        private const val admobRewardedUnitId = "ca-app-pub-3940256099942544/5224354917"
    }
}
