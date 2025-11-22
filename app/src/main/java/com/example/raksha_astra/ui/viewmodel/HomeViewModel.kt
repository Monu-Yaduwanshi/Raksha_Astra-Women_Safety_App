package com.example.raksha_astra.ui.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val isSendingSos: Boolean = false,
    val lastSosTimeMillis: Long? = null
)

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    fun toggleSos(now: Long = System.currentTimeMillis()) {
        val sending = !_state.value.isSendingSos
        _state.value = _state.value.copy(isSendingSos = sending, lastSosTimeMillis = if (sending) now else _state.value.lastSosTimeMillis)
        // TODO: call Repository to send FCM + Firebase update
    }
}
