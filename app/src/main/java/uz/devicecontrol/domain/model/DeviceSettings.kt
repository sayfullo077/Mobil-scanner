package uz.devicecontrol.domain.model

data class DeviceSettings(
    val brightness: Int,          // 0..255
    val canWriteSettings: Boolean,
    val mediaVolume: Int,
    val maxMediaVolume: Int,
)
