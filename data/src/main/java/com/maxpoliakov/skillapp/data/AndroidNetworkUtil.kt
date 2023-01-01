package com.maxpoliakov.skillapp.data

import android.net.ConnectivityManager
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import javax.inject.Inject

class AndroidNetworkUtil @Inject constructor(
    private val connectivityManager: ConnectivityManager,
) : NetworkUtil {
    override val isConnected: Boolean
        get() = connectivityManager.activeNetworkInfo?.isConnected == true
}
