package com.pri.artsysearchapp.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SnackBarManager {
    private var snackbarHostState: SnackbarHostState? = null
    private var scope: CoroutineScope? = null

    fun init(hostState: SnackbarHostState, coroutineScope: CoroutineScope) {
        snackbarHostState = hostState
        scope = coroutineScope
    }

    fun showMessage(message: String) {
        snackbarHostState?.let { host ->
            scope?.launch {
                host.showSnackbar(message)
            }
        }
    }

    fun showMessage(message: String, actionLabel: String? = null, duration: SnackbarDuration = SnackbarDuration.Short) {
        snackbarHostState?.let { host ->
            scope?.launch {
                host.showSnackbar(message = message, actionLabel = actionLabel, duration = duration)
            }
        }
    }
}