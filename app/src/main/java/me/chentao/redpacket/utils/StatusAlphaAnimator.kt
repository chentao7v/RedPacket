package me.chentao.redpacket.utils

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import androidx.core.view.isVisible

/**
 * create by chentao on 2024-01-17.
 */
class StatusAlphaAnimator(private val ivStatus: View) {

  private val statusAnimator: ObjectAnimator by lazy {
    ObjectAnimator.ofFloat(ivStatus, "alpha", 0f, 1f).apply {
      duration = 1000
      repeatCount = 5
      repeatMode = ValueAnimator.REVERSE
    }
  }

  /**
   * 用户手动取消动画
   */
  private var userCancel = false

  fun cancelManual() {
    userCancel = true
    statusAnimator.cancel()
    ivStatus.alpha = 1.0f
  }

  fun refreshStatus(isOpen: Boolean) {
    if (isOpen) {
      // 打开的
      ivStatus.isVisible = false
      if (statusAnimator.isRunning) {
        statusAnimator.cancel()
      }
    } else {
      // 未打开
      ivStatus.isVisible = true
      if (!userCancel) {
        statusAnimator.start()
      }
    }
  }

  fun cancel() {
    if (statusAnimator.isRunning) {
      statusAnimator.cancel()
    }
  }

}