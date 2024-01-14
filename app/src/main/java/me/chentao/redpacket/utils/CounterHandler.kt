package me.chentao.redpacket.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import timber.log.Timber

/**
 * create by chentao on 2024-01-14.
 */
class CounterHandler(private val block: () -> Boolean) : Handler(Looper.getMainLooper()) {

  companion object {
    const val WHAT_LOOP = 1
  }

  private var isCanceled = false

  private var intervalMills = 0L
  private var counterDown = 0

  override fun handleMessage(msg: Message) {
    if (msg.what == WHAT_LOOP) {
      this.counterDown--
      if (block() || this.counterDown <= 0) {
        stop()
      } else {
        this.sendEmptyMessageDelayed(WHAT_LOOP, intervalMills)
      }
    }
  }

  fun start(counter: Int, intervalMills: Long = 0) {
    this.intervalMills = intervalMills
    this.counterDown = counter

    if (isCanceled) {
      stop()
    } else {
      this.sendEmptyMessageDelayed(WHAT_LOOP, intervalMills)
      isCanceled = false
    }
  }

  fun stop() {
    Timber.d("停止计数，当前数量 -> $counterDown")
    this.removeCallbacksAndMessages(null)
    isCanceled = true
  }

}