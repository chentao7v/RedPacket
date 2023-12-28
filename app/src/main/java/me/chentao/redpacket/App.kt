package me.chentao.redpacket

import android.app.Application
import timber.log.Timber

/**
 * create by chentao on 2023-12-27.
 */
class App : Application() {

  override fun onCreate() {
    super.onCreate()

    Timber.plant(Timber.DebugTree())
  }
}