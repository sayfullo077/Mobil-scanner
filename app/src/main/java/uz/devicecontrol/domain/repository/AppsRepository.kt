package uz.devicecontrol.domain.repository

import uz.devicecontrol.domain.model.InstalledApp

interface AppsRepository {
    suspend fun installedApps(includeSystem: Boolean): List<InstalledApp>
    fun launchApp(packageName: String): Boolean
}
