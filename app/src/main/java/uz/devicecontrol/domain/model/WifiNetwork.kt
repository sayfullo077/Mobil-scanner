package uz.devicecontrol.domain.model

data class WifiNetwork(
    val ssid: String,
    val bssid: String,
    val signalLevel: Int,   // 0..4
    val frequencyMhz: Int,
    val capabilities: String,
)

data class NetworkStatus(
    val wifiEnabled: Boolean,
    val connectionType: String,  // "Wi-Fi", "Mobil internet", "Ulanmagan" ...
)
