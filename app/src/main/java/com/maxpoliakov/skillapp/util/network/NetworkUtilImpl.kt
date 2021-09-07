package com.maxpoliakov.skillapp.util.network

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkUtilImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkUtil {

    override val isConnected: Boolean
        get() {
            val connectivityManager = context.getSystemService<ConnectivityManager>()
            val networkInfo = connectivityManager?.activeNetworkInfo

            return networkInfo != null && networkInfo.isConnected
        }
}
