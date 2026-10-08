package uz.devicecontrol.domain.model

data class InstalledApp(
    val label: String,
    val packageName: String,
    val isSystemApp: Boolean,
    val requestedPermissions: List<String>,
)
