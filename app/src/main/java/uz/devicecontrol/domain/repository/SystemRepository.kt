package uz.devicecontrol.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.devicecontrol.domain.model.SystemInfo

interface SystemRepository {
    /** Tizim ma'lumotini kuzatish (batareya real vaqtda yangilanadi). */
    fun observeSystemInfo(): Flow<SystemInfo>
}
