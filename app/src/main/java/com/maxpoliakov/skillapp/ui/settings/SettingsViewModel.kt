package com.maxpoliakov.skillapp.ui.settings

import androidx.lifecycle.ViewModel
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val billingRepository: BillingRepository,
) : ViewModel() {
    val subscriptionState get() = billingRepository.subscriptionState
}
