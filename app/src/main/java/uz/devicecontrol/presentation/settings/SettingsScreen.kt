package uz.devicecontrol.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.devicecontrol.presentation.common.InfoCard

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        InfoCard("Ekran yorqinligi") {
            if (state.canWriteSettings) {
                Text("Yorqinlik: ${state.brightness} / 255")
                Slider(
                    value = state.brightness.toFloat(),
                    onValueChange = { viewModel.setBrightness(it.toInt()) },
                    valueRange = 0f..255f,
                )
            } else {
                Text("Yorqinlikni o'zgartirish uchun maxsus ruxsat kerak.")
                Button(
                    onClick = { viewModel.requestWritePermission() },
                    modifier = Modifier.padding(top = 8.dp),
                ) { Text("Ruxsat berish") }
            }
        }
        InfoCard("Media ovoz balandligi") {
            Text("Ovoz: ${state.mediaVolume} / ${state.maxMediaVolume}")
            Slider(
                value = state.mediaVolume.toFloat(),
                onValueChange = { viewModel.setMediaVolume(it.toInt()) },
                valueRange = 0f..state.maxMediaVolume.toFloat().coerceAtLeast(1f),
            )
        }
        InfoCard("Uchish rejimi") {
            Text("Uchish rejimini Android bevosita o'zgartirishga ruxsat bermaydi — sozlamalar oynasi ochiladi.")
            OutlinedButton(
                onClick = { viewModel.openAirplaneMode() },
                modifier = Modifier.padding(top = 8.dp),
            ) { Text("Uchish rejimi sozlamalari") }
        }
    }
}
