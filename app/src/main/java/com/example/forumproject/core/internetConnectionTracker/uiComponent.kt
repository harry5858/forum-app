package com.example.forumproject.core.internetConnectionTracker

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ConnectivityStatus() {
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    var visibility by remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            visibility = true
        } else {
            delay(2000)
            visibility = false
        }
    }
    ConnectivityStatusText(isConnected = isConnected)
}

@Composable
fun ConnectivityStatusText(
    isConnected: Boolean
) {
    val message = if (isConnected) "Back Online!" else "No Internet Connection!"

    Text(
        text = message,
        fontSize = 25.sp
    )
}