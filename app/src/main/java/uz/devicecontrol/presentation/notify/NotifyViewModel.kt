package uz.devicecontrol.presentation.notify

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.devicecontrol.domain.repository.NotificationRepository
import javax.inject.Inject

@HiltViewModel
class NotifyViewModel @Inject constructor(
    private val repository: NotificationRepository,
) : ViewModel() {
    fun send() = repository.showDemoNotification(
        title = "DeviceControl",
        message = "Bu ilova tomonidan yuborilgan namuna bildirishnoma.",
    )
}
