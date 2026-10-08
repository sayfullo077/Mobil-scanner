package uz.devicecontrol.domain.repository

import uz.devicecontrol.domain.model.DeviceSettings

interface SettingsRepository {
    fun current(): DeviceSettings
    fun setBrightness(value: Int): Boolean
    fun setMediaVolume(value: Int)
    fun openWriteSettingsPermission()
    fun openAirplaneModeSettings()
}
