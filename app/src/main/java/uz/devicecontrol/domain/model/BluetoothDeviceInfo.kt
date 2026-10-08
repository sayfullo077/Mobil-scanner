package uz.devicecontrol.domain.model

data class BluetoothDeviceInfo(
    val name: String,
    val address: String,
    val bonded: Boolean,
)
