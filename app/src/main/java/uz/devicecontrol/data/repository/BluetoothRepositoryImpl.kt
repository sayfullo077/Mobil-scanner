package uz.devicecontrol.data.repository

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import uz.devicecontrol.domain.model.BluetoothDeviceInfo
import uz.devicecontrol.domain.repository.BluetoothRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : BluetoothRepository {

    private val adapter: BluetoothAdapter? by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        manager?.adapter
    }

    override fun isSupported(): Boolean = adapter != null

    override fun isEnabled(): Boolean = adapter?.isEnabled == true

    @SuppressLint("MissingPermission")
    override fun bondedDevices(): List<BluetoothDeviceInfo> = try {
        adapter?.bondedDevices?.map { it.toInfo(bonded = true) } ?: emptyList()
    } catch (e: SecurityException) {
        emptyList()
    }

    @SuppressLint("MissingPermission")
    override fun discoverDevices(): Flow<List<BluetoothDeviceInfo>> = callbackFlow {
        val found = LinkedHashMap<String, BluetoothDeviceInfo>()
        adapter?.bondedDevices?.forEach { found[it.address] = it.toInfo(bonded = true) }
        trySend(found.values.toList())

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                if (intent?.action == BluetoothDevice.ACTION_FOUND) {
                    val device: BluetoothDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }
                    device?.let {
                        found[it.address] = it.toInfo(bonded = false)
                        trySend(found.values.toList())
                    }
                }
            }
        }
        context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        runCatching { adapter?.startDiscovery() }

        awaitClose {
            runCatching { adapter?.cancelDiscovery() }
            runCatching { context.unregisterReceiver(receiver) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun BluetoothDevice.toInfo(bonded: Boolean) = BluetoothDeviceInfo(
        name = try { name ?: "(nomsiz)" } catch (e: SecurityException) { "(nomsiz)" },
        address = address,
        bonded = bonded,
    )
}
