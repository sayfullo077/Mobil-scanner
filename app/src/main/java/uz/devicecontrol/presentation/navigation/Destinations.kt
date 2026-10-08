package uz.devicecontrol.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(val route: String, val label: String, val icon: ImageVector) {
    System("system", "Tizim", Icons.Filled.PhoneAndroid),
    Network("network", "Tarmoq", Icons.Filled.Wifi),
    Settings("settings", "Sozlama", Icons.Filled.Settings),
    Apps("apps", "Ilovalar", Icons.Filled.Apps),
    Files("files", "Fayllar", Icons.Filled.Folder),
    Notify("notify", "Xabar", Icons.Filled.Notifications),
}
