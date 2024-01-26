package me.chentao.redpacket.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.PowerManager
import timber.log.Timber

/**
 * create by chentao on 2024-01-25.
 */
object WakeLocker {

  private const val WAKE_LOCK_TAG = "redpacket:wakeup"
  private const val KEYGUARD_TAG = "redpacket:unlock"


  private val powerManager by lazy {
    app.getSystemService(Context.POWER_SERVICE) as PowerManager
  }

  private val keyguardManager by lazy {
    app.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
  }

  private val keyguard by lazy {
    keyguardManager.newKeyguardLock(KEYGUARD_TAG)
  }

  /**
   * 唤醒锁
   */
  @SuppressLint("WakelockTimeout")
  fun newWakeLockAcquire(mills: Long = -1): PowerManager.WakeLock {
    val wakeLock = powerManager.newWakeLock(
      PowerManager.ACQUIRE_CAUSES_WAKEUP
          or PowerManager.SCREEN_DIM_WAKE_LOCK, WAKE_LOCK_TAG
    )
    if (mills < 0) {
      wakeLock.acquire()
    } else {
      wakeLock.acquire(mills)
    }

    return wakeLock
  }

  /**
   * 解开屏幕锁
   */
  fun requestDismissKeyguard(context: Activity) {
    // 解锁屏幕
    // 有锁屏密码，则需要输入密码；无则直接解锁
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      keyguardManager.requestDismissKeyguard(context, object : KeyguardManager.KeyguardDismissCallback() {
        override fun onDismissSucceeded() {
          Timber.e("onDismissSucceeded")
        }

        override fun onDismissCancelled() {
          Timber.e("onDismissCancelled")
        }

        override fun onDismissError() {
          Timber.e("onDismissError")
        }
      })
    }
  }

  fun disableKeyguard() {
    keyguard.disableKeyguard()
  }

  fun reenableKeyguard() {
    keyguard.reenableKeyguard()
  }


  /**
   * 是否锁屏。不管有没有设置密码
   */
  fun isKeyguardLocked(): Boolean {
    return keyguardManager.isKeyguardLocked
  }

  /**
   * 手机锁屏，且需要密码才能解锁
   */
  fun isDeviceLocked(): Boolean {
    return keyguardManager.isDeviceLocked
  }


}