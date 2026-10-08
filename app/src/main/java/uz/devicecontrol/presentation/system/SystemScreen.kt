package uz.devicecontrol.presentation.system

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import uz.devicecontrol.domain.model.SystemInfo
import uz.devicecontrol.presentation.common.InfoCard
import uz.devicecontrol.presentation.common.InfoRow
import uz.devicecontrol.presentation.common.formatBytes

@Composable
fun SystemScreen(viewModel: SystemViewModel = hiltViewModel()) {
    val info by viewModel.state.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        val data = info
        if (data == null) {
            Text("Yuklanmoqda...", modifier = Modifier.padding(16.dp))
        } else {
            Content(data)
        }
    }
}

@Composable
private fun Content(info: SystemInfo) {
    InfoCard("Qurilma") {
        InfoRow("Ishlab chiqaruvchi", info.manufacturer)
        InfoRow("Model", info.model)
        InfoRow("Android", "${info.androidVersion} (API ${info.sdkInt})")
    }
    InfoCard("Batareya") {
        InfoRow("Zaryad", "${info.batteryPercent}%")
        InfoRow("Holat", if (info.isCharging) "Quvvatlanmoqda" else "Quvvatlanmayapti")
        LinearProgressIndicator(
            progress = { info.batteryPercent / 100f },
            modifier = Modifier.padding(top = 8.dp),
        )
    }
    InfoCard("Xotira (RAM)") {
        val usedRam = info.totalRamBytes - info.availableRamBytes
        InfoRow("Jami", formatBytes(info.totalRamBytes))
        InfoRow("Bo'sh", formatBytes(info.availableRamBytes))
        InfoRow("Band", formatBytes(usedRam))
    }
    InfoCard("Xotira (Storage)") {
        InfoRow("Jami", formatBytes(info.totalStorageBytes))
        InfoRow("Bo'sh", formatBytes(info.availableStorageBytes))
    }
    InfoCard("Protsessor") {
        InfoRow("Yadrolar", info.cpuCores.toString())
        InfoRow("ABI", info.supportedAbis.joinToString(", "))
    }
}
