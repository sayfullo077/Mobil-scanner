package uz.devicecontrol.presentation.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import uz.devicecontrol.domain.model.SystemInfo
import uz.devicecontrol.domain.repository.SystemRepository
import javax.inject.Inject

@HiltViewModel
class SystemViewModel @Inject constructor(
    systemRepository: SystemRepository,
) : ViewModel() {

    val state: StateFlow<SystemInfo?> = systemRepository.observeSystemInfo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
