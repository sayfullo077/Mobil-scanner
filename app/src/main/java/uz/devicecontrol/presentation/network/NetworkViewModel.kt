package uz.devicecontrol.presentation.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.devicecontrol.domain.model.NetworkStatus
import uz.devicecontrol.domain.model.WifiNetwork
import uz.devicecontrol.domain.repository.NetworkRepository
import javax.inject.Inject

data class NetworkUiState(
    val status: NetworkStatus = NetworkStatus(false, "..."),
    val networks: List<WifiNetwork> = emptyList(),
    val scanning: Boolean = false,
)

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val repository: NetworkRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(NetworkUiState())
    val state: StateFlow<NetworkUiState> = _state.asStateFlow()

    fun refresh() {
        _state.value = _state.value.copy(
            status = repository.currentStatus(),
            networks = repository.scanResults(),
        )
    }

    fun scan() {
        viewModelScope.launch {
            _state.value = _state.value.copy(scanning = true)
            repository.startScan()
            // Natijalar tizim tomonidan biroz keyin tayyor bo'ladi.
            kotlinx.coroutines.delay(3000)
            _state.value = _state.value.copy(
                status = repository.currentStatus(),
                networks = repository.scanResults(),
                scanning = false,
            )
        }
    }

    fun openWifiSettings() = repository.openWifiSettings()
}
