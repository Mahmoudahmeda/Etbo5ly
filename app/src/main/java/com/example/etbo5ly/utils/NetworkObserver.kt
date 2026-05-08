package com.example.etbo5ly.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

fun observeNetworkConnectivity(context: Context): Flow<Boolean> = callbackFlow {

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Send current state immediately when flow starts
    val currentState = isInternetAvailable(context)
    trySend(currentState)

    val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            // Internet became available
            trySend(true)
        }

        override fun onLost(network: Network) {
            // Internet was lost
            trySend(false)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            // Connectivity changed — recheck
            val hasInternet = networkCapabilities
                .hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            trySend(hasInternet)
        }
    }

    val request = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(request, callback)

    // When flow is cancelled unregister to avoid memory leaks
    awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
    }

}.distinctUntilChanged() // only emit when state actually changes