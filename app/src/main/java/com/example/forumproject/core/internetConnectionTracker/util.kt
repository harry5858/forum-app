package com.example.forumproject.core.internetConnectionTracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun currentConnectionState(): ConnectionState {
    val context = LocalContext.current
    return remember { context.currentConnectivityState }
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current
    return produceState(initialValue = context.currentConnectivityState) {
        context.observeConnectivityAsFlow().distinctUntilChanged().collect { value = it }
    }
}