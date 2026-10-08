package uz.devicecontrol.domain.model

/**
 * Qurilma tizimi haqidagi ma'lumotlar. Hammasi faqat shu telefonning o'zidan olinadi.
 */
data class SystemInfo(
    val manufacturer: String,
    val model: String,
    val androidVersion: String,
    val sdkInt: Int,
    val batteryPercent: Int,
    val isCharging: Boolean,
    val totalRamBytes: Long,
    val availableRamBytes: Long,
    val totalStorageBytes: Long,
    val availableStorageBytes: Long,
    val cpuCores: Int,
    val supportedAbis: List<String>,
)
