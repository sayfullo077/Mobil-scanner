package uz.devicecontrol.domain.repository

interface NotificationRepository {
    fun showDemoNotification(title: String, message: String)
}
