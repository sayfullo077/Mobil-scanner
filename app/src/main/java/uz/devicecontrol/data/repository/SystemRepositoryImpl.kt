package uz.devicecontrol.data.repository

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import uz.devicecontrol.domain.model.SystemInfo
import uz.devicecontrol.domain.repository.SystemRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SystemRepository {

    override fun observeSystemInfo(): Flow<SystemInfo> = callbackFlow {
        fun emit() {
            trySend(buildSnapshot())
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) = emit()
        }
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        emit()
        awaitClose { runCatching { context.unregisterReceiver(receiver) } }
    }

    private fun buildSnapshot(): SystemInfo {
        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val percent = if (level >= 0 && scale > 0) level * 100 / scale else 0
        val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
            status == BatteryManager.BATTERY_STATUS_FULL

        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mem = ActivityManager.MemoryInfo().also { am.getMemoryInfo(it) }

        val statFs = StatFs(Environment.getDataDirectory().path)
        val totalStorage = statFs.blockCountLong * statFs.blockSizeLong
        val availStorage = statFs.availableBlocksLong * statFs.blockSizeLong

        return SystemInfo(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE,
            sdkInt = Build.VERSION.SDK_INT,
            batteryPercent = percent,
            isCharging = charging,
            totalRamBytes = mem.totalMem,
            availableRamBytes = mem.availMem,
            totalStorageBytes = totalStorage,
            availableStorageBytes = availStorage,
            cpuCores = Runtime.getRuntime().availableProcessors(),
            supportedAbis = Build.SUPPORTED_ABIS.toList(),
        )
    }
}
