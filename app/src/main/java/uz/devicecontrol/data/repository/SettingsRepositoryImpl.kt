package uz.devicecontrol.data.repository

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.devicecontrol.domain.model.DeviceSettings
import uz.devicecontrol.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository {

    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun current(): DeviceSettings {
        val brightness = try {
            Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Settings.SettingNotFoundException) {
            0
        }
        return DeviceSettings(
            brightness = brightness,
            canWriteSettings = Settings.System.canWrite(context),
            mediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
            maxMediaVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
        )
    }

    override fun setBrightness(value: Int): Boolean {
        if (!Settings.System.canWrite(context)) return false
        val clamped = value.coerceIn(0, 255)
        return try {
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL,
            )
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                clamped,
            )
        } catch (e: Exception) {
            false
        }
    }

    override fun setMediaVolume(value: Int) {
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            value.coerceIn(0, max),
            0,
        )
    }

    override fun openWriteSettingsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    override fun openAirplaneModeSettings() {
        val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
