package com.soonodekar.myapp1.recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ConnectionCheckReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        val isConnected = networkInfo != null && networkInfo.isConnected

        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isInternetAvailable = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        onConnectivityChangeListener!!.onConnectivityChanges(isConnected, isInternetAvailable)
    }

    var onConnectivityChangeListener : OnConnectivityChangeListener ? =null

    interface OnConnectivityChangeListener{
        fun onConnectivityChanges(isConnected : Boolean, isInternetAvailable : Boolean )
    }
}