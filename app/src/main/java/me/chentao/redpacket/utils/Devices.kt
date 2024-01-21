package me.chentao.redpacket.utils

import android.provider.Settings
import timber.log.Timber
import java.util.UUID

/**
 * create by chentao on 2024-01-21.
 */
object Devices {

  fun deviceId(): String {
    return setupDeviceId()
  }

  private fun setupDeviceId(): String {
    return try {
      val androidId = Settings.Secure.getString(
        app.contentResolver,
        Settings.Secure.ANDROID_ID
      )
      if (androidId.isNullOrEmpty()) {
        fakeDeviceId()
      } else {
        Timber.d("android Id -> $androidId")
        androidId
      }
    } catch (e: Exception) {
      fakeDeviceId()
    }
  }

  private fun fakeDeviceId(): String {
    val fakeId = UUID.randomUUID().toString().substring(0, 16)
    Timber.d("fakeId -> $fakeId")
    return fakeId
  }
}