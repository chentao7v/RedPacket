package me.chentao.redpacket

import android.app.Application
import me.chentao.redpacket.notify.initChannelIfNecessary
import me.chentao.redpacket.utils.app
import timber.log.Timber

/**
 * create by chentao on 2023-12-27.
 */
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    app = this

    Timber.plant(Timber.DebugTree())

    initChannelIfNecessary()
  }
}