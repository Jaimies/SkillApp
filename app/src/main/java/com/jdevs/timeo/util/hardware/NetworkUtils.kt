package com.jdevs.timeo.util.hardware

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import javax.inject.Inject

class NetworkUtils @Inject constructor(context: Context) {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    @Suppress("DEPRECATION")
    fun hasNetworkConnection(): Boolean {
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}