package uz.devicecontrol.presentation.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.devicecontrol.domain.model.InstalledApp
import uz.devicecontrol.domain.repository.AppsRepository
import javax.inject.Inject

data class AppsUiState(
    val apps: List<InstalledApp> = emptyList(),
    val includeSystem: Boolean = false,
    val loading: Boolean = true,
)

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val repository: AppsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AppsUiState())
    val state: StateFlow<AppsUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val apps = repository.installedApps(_state.value.includeSystem)
            _state.value = _state.value.copy(apps = apps, loading = false)
        }
    }

    fun toggleSystem() {
        _state.value = _state.value.copy(includeSystem = !_state.value.includeSystem)
        load()
    }

    fun launch(packageName: String) = repository.launchApp(packageName)
}
