package uz.devicecontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.devicecontrol.presentation.apps.AppsScreen
import uz.devicecontrol.presentation.files.FilesScreen
import uz.devicecontrol.presentation.navigation.Destination
import uz.devicecontrol.presentation.network.NetworkScreen
import uz.devicecontrol.presentation.notify.NotifyScreen
import uz.devicecontrol.presentation.settings.SettingsScreen
import uz.devicecontrol.presentation.system.SystemScreen
import uz.devicecontrol.presentation.theme.DeviceControlTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeviceControlTheme {
                AppRoot()
            }
        }
    }
}

@Composable
private fun AppRoot() {
    val navController = rememberNavController()
    val destinations = Destination.entries
    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            NavigationBar {
                destinations.forEach { dest ->
                    val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(dest.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(dest.icon, contentDescription = dest.label) },
                        label = { Text(dest.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destination.System.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(Destination.System.route) { SystemScreen() }
            composable(Destination.Network.route) { NetworkScreen() }
            composable(Destination.Settings.route) { SettingsScreen() }
            composable(Destination.Apps.route) { AppsScreen() }
            composable(Destination.Files.route) { FilesScreen() }
            composable(Destination.Notify.route) { NotifyScreen() }
        }
    }
}
