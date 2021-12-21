package com.maxpoliakov.skillapp.util.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

typealias OnAdLoadedListener = (RewardedAdManager.LoadingState) -> Unit

class RewardedAdManager {
    private var mRewardedAd: RewardedAd? = null
    var loadingState = LoadingState.Loading

    private var onAdLoadedListener: OnAdLoadedListener? = null

    fun loadAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            admobRewardedUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    loadingState = LoadingState.FailedToLoad
                    onAdLoadedListener?.invoke(loadingState)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                    loadingState = LoadingState.Loaded
                    onAdLoadedListener?.invoke(loadingState)
                }
            })
    }

    fun showAdIfAvailable(activity: Activity, onUserEarnedRewardListener: (RewardItem) -> Unit) {
        mRewardedAd?.run {
            show(activity, onUserEarnedRewardListener)
            mRewardedAd = null
        }
    }

    fun setOnAdLoadedListener(onAdLoaded: OnAdLoadedListener) {
        this.onAdLoadedListener = onAdLoaded
    }

    enum class LoadingState {
        Loading, Loaded, FailedToLoad
    }

    companion object {
        private const val admobRewardedUnitId = "ca-app-pub-3940256099942544/5224354917"
    }
}
