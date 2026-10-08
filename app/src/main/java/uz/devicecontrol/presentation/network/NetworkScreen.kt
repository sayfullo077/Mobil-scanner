package uz.devicecontrol.presentation.network

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import uz.devicecontrol.presentation.common.InfoCard
import uz.devicecontrol.presentation.common.InfoRow

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NetworkScreen(
    viewModel: NetworkViewModel = hiltViewModel(),
    bluetoothViewModel: BluetoothViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val btState by bluetoothViewModel.state.collectAsStateWithLifecycle()

    val locationPermission = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val btPermissions = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT,
            )
        } else {
            listOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    )

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        InfoCard("Tarmoq holati") {
            InfoRow("Wi-Fi", if (state.status.wifiEnabled) "Yoqilgan" else "O'chirilgan")
            InfoRow("Ulanish turi", state.status.connectionType)
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = {
                    if (locationPermission.status.isGranted) viewModel.scan()
                    else locationPermission.launchPermissionRequest()
                },
            ) { Text(if (state.scanning) "Skaner..." else "Wi-Fi skaner") }
            OutlinedButton(onClick = { viewModel.openWifiSettings() }) {
                Text("Wi-Fi sozlamalari")
            }
        }

        if (!locationPermission.status.isGranted) {
            Text(
                "Wi-Fi tarmoqlarni ko'rsatish uchun joylashuv ruxsati kerak (Android qoidasi).",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        if (state.scanning) CircularProgressIndicator(modifier = Modifier.padding(16.dp))

        state.networks.forEach { net ->
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)) {
                Text(net.ssid, fontWeight = FontWeight.Bold)
                Text("Signal: ${net.signalLevel}/4  •  ${net.frequencyMhz} MHz")
                HorizontalDivider(modifier = Modifier.padding(top = 6.dp))
            }
        }

        InfoCard("Bluetooth") {
            if (!btState.supported) {
                Text("Bu qurilmada Bluetooth qo'llab-quvvatlanmaydi.")
            } else {
                InfoRow("Holat", if (btState.enabled) "Yoqilgan" else "O'chirilgan")
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(onClick = {
                        if (btPermissions.allPermissionsGranted) {
                            bluetoothViewModel.refresh()
                            bluetoothViewModel.scan()
                        } else {
                            btPermissions.launchMultiplePermissionRequest()
                        }
                    }) { Text(if (btState.scanning) "Skaner..." else "Qurilmalarni skaner") }
                    if (btState.scanning) {
                        OutlinedButton(onClick = { bluetoothViewModel.stopScan() }) { Text("To'xtatish") }
                    }
                }
                btState.devices.forEach { device ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(device.name + if (device.bonded) " (ulangan)" else "", fontWeight = FontWeight.Medium)
                        Text(device.address)
                    }
                }
            }
        }
    }
}
