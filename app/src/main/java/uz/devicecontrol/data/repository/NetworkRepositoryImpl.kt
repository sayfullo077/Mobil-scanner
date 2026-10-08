package uz.devicecontrol.data.repository

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.devicecontrol.domain.model.NetworkStatus
import uz.devicecontrol.domain.model.WifiNetwork
import uz.devicecontrol.domain.repository.NetworkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkRepository {

    private val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun currentStatus(): NetworkStatus {
        val type = activeConnectionType()
        return NetworkStatus(
            wifiEnabled = wifiManager.isWifiEnabled,
            connectionType = type,
        )
    }

    private fun activeConnectionType(): String {
        val network = connectivityManager.activeNetwork ?: return "Ulanmagan"
        val caps = connectivityManager.getNetworkCapabilities(network) ?: return "Ulanmagan"
        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobil internet"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            else -> "Boshqa"
        }
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    override fun scanResults(): List<WifiNetwork> {
        return try {
            wifiManager.scanResults.map {
                @Suppress("DEPRECATION")
                val ssid = it.SSID.ifBlank { "(yashirin tarmoq)" }
                WifiNetwork(
                    ssid = ssid,
                    bssid = it.BSSID ?: "",
                    signalLevel = WifiManager.calculateSignalLevel(it.level, 5),
                    frequencyMhz = it.frequency,
                    capabilities = it.capabilities ?: "",
                )
            }.sortedByDescending { it.signalLevel }
        } catch (e: SecurityException) {
            emptyList()
        }
    }

    @Suppress("DEPRECATION")
    override fun startScan(): Boolean = try {
        wifiManager.startScan()
    } catch (e: Exception) {
        false
    }

    override fun openWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
