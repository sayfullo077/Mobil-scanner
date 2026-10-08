package uz.devicecontrol.domain.repository

import uz.devicecontrol.domain.model.NetworkStatus
import uz.devicecontrol.domain.model.WifiNetwork

interface NetworkRepository {
    fun currentStatus(): NetworkStatus
    /** Oxirgi Wi-Fi skan natijalari. Location ruxsati kerak. */
    fun scanResults(): List<WifiNetwork>
    fun startScan(): Boolean
    fun openWifiSettings()
}
