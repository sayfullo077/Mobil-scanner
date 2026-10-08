package uz.devicecontrol.presentation.notify

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotifyScreen(viewModel: NotifyViewModel = hiltViewModel()) {
    val notifPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
    } else null

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Bildirishnoma yuborish namunasi")
        Button(onClick = {
            if (notifPermission == null || notifPermission.status.isGranted) {
                viewModel.send()
            } else {
                notifPermission.launchPermissionRequest()
            }
        }) { Text("Bildirishnoma yuborish") }

        if (notifPermission != null && !notifPermission.status.isGranted) {
            Text("Bildirishnoma yuborish uchun ruxsat bering, so'ng tugmani qayta bosing.")
        }
    }
}
