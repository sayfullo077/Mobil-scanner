package uz.devicecontrol.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.devicecontrol.domain.model.BluetoothDeviceInfo

interface BluetoothRepository {
    fun isSupported(): Boolean
    fun isEnabled(): Boolean
    fun bondedDevices(): List<BluetoothDeviceInfo>
    /** Atrofdagi qurilmalarni skaner qilish; topilgan qurilmalar oqimda qaytadi. */
    fun discoverDevices(): Flow<List<BluetoothDeviceInfo>>
}
