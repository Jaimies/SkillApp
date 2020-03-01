package com.jdevs.timeo.util.hardware

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

@Suppress("DEPRECATION")
val Context.hasNetworkConnection: Boolean
    get() {
        val connectivityManager = getSystemService<ConnectivityManager>()
        val networkInfo = connectivityManager?.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
