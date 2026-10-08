package uz.devicecontrol.data.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.devicecontrol.domain.model.InstalledApp
import uz.devicecontrol.domain.repository.AppsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppsRepository {

    private val pm: PackageManager = context.packageManager

    override suspend fun installedApps(includeSystem: Boolean): List<InstalledApp> =
        withContext(Dispatchers.IO) {
            val flags = PackageManager.GET_PERMISSIONS
            val packages = pm.getInstalledPackages(flags)
            packages.mapNotNull { pkg ->
                val appInfo = pkg.applicationInfo ?: return@mapNotNull null
                val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                if (!includeSystem && isSystem) return@mapNotNull null
                InstalledApp(
                    label = pm.getApplicationLabel(appInfo).toString(),
                    packageName = pkg.packageName,
                    isSystemApp = isSystem,
                    requestedPermissions = pkg.requestedPermissions?.toList() ?: emptyList(),
                )
            }.sortedBy { it.label.lowercase() }
        }

    override fun launchApp(packageName: String): Boolean {
        val intent = pm.getLaunchIntentForPackage(packageName) ?: return false
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }
}
