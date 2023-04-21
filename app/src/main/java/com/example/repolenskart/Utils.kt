package com.example.repolenskart

import android.app.Application
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat

fun isNetworkAvailable(app: Application): Boolean {
    val connectivityManager =
        ContextCompat.getSystemService(app, ConnectivityManager::class.java) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

class NoNetworkException(exMsg: String = NO_NETWORK) : RuntimeException(exMsg) {
    companion object {
        const val NO_NETWORK = "NO_NETWORK"
    }
}