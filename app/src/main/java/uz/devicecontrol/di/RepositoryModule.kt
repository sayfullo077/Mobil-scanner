package uz.devicecontrol.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.devicecontrol.data.repository.AppsRepositoryImpl
import uz.devicecontrol.data.repository.BluetoothRepositoryImpl
import uz.devicecontrol.data.repository.FileRepositoryImpl
import uz.devicecontrol.data.repository.NetworkRepositoryImpl
import uz.devicecontrol.data.repository.NotificationRepositoryImpl
import uz.devicecontrol.data.repository.SettingsRepositoryImpl
import uz.devicecontrol.data.repository.SystemRepositoryImpl
import uz.devicecontrol.domain.repository.AppsRepository
import uz.devicecontrol.domain.repository.BluetoothRepository
import uz.devicecontrol.domain.repository.FileRepository
import uz.devicecontrol.domain.repository.NetworkRepository
import uz.devicecontrol.domain.repository.NotificationRepository
import uz.devicecontrol.domain.repository.SettingsRepository
import uz.devicecontrol.domain.repository.SystemRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindSystemRepository(impl: SystemRepositoryImpl): SystemRepository

    @Binds @Singleton
    abstract fun bindNetworkRepository(impl: NetworkRepositoryImpl): NetworkRepository

    @Binds @Singleton
    abstract fun bindBluetoothRepository(impl: BluetoothRepositoryImpl): BluetoothRepository

    @Binds @Singleton
    abstract fun bindAppsRepository(impl: AppsRepositoryImpl): AppsRepository

    @Binds @Singleton
    abstract fun bindFileRepository(impl: FileRepositoryImpl): FileRepository

    @Binds @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}
