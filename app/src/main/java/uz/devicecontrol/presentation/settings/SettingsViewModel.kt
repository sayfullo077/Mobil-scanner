package uz.devicecontrol.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.devicecontrol.domain.model.DeviceSettings
import uz.devicecontrol.domain.repository.SettingsRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(repository.current())
    val state: StateFlow<DeviceSettings> = _state.asStateFlow()

    fun refresh() { _state.value = repository.current() }

    fun setBrightness(value: Int) {
        repository.setBrightness(value)
        refresh()
    }

    fun setMediaVolume(value: Int) {
        repository.setMediaVolume(value)
        refresh()
    }

    fun requestWritePermission() = repository.openWriteSettingsPermission()
    fun openAirplaneMode() = repository.openAirplaneModeSettings()
}
