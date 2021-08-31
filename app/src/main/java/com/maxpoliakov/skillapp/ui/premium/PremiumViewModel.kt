package com.maxpoliakov.skillapp.ui.premium

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor() : ViewModel() {
    private val _showSubscriptionPrompt = SingleLiveEvent<Nothing>()
    val showSubscriptionPrompt: LiveData<Nothing> get() = _showSubscriptionPrompt

    fun showSubscriptionPrompt() = _showSubscriptionPrompt.call()
}
