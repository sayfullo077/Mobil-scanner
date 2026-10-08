package uz.devicecontrol.presentation.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.devicecontrol.domain.model.BluetoothDeviceInfo
import uz.devicecontrol.domain.repository.BluetoothRepository
import javax.inject.Inject

data class BluetoothUiState(
    val supported: Boolean = true,
    val enabled: Boolean = false,
    val devices: List<BluetoothDeviceInfo> = emptyList(),
    val scanning: Boolean = false,
)

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val repository: BluetoothRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BluetoothUiState())
    val state: StateFlow<BluetoothUiState> = _state.asStateFlow()

    private var scanJob: Job? = null

    fun refresh() {
        _state.value = _state.value.copy(
            supported = repository.isSupported(),
            enabled = repository.isEnabled(),
        )
    }

    fun scan() {
        if (!repository.isSupported()) return
        scanJob?.cancel()
        _state.value = _state.value.copy(scanning = true, enabled = repository.isEnabled())
        scanJob = viewModelScope.launch {
            repository.discoverDevices().collect { devices ->
                _state.value = _state.value.copy(devices = devices)
            }
        }
    }

    fun stopScan() {
        scanJob?.cancel()
        scanJob = null
        _state.value = _state.value.copy(scanning = false)
    }

    override fun onCleared() {
        super.onCleared()
        scanJob?.cancel()
    }
}
