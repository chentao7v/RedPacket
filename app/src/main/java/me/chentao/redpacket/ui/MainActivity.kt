package me.chentao.redpacket.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.chentao.redpacket.R
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivityMainBinding
import me.chentao.redpacket.service.RedPacketService
import me.chentao.redpacket.utils.AccessibilityTools

class MainActivity : BaseActivity<ActivityMainBinding>() {

  override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

  private val statusAnimator: ObjectAnimator by lazy {
    ObjectAnimator.ofFloat(binding.ivStatus, "alpha", 0f, 1f).apply {
      duration = 1000
      repeatCount = 10
      repeatMode = ValueAnimator.REVERSE
    }
  }

  /**
   * 用户手动取消动画
   */
  private var userCancel = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.robot.setOnClickListener { AccessibilityTools.gotoSettingsUI(this) }
    binding.settings.setOnClickListener { SettingsActivity.launch(this) }
    binding.ivStatus.setOnClickListener { showRobotDialog() }
  }

  private fun showRobotDialog() {
    statusAnimator.cancel()
    binding.ivStatus.alpha = 1.0f

    userCancel = true

    MaterialAlertDialogBuilder(this)
      .setTitle(R.string.alert_default_title)
      .setMessage(R.string.robot_close)
      .setPositiveButton(R.string.i_know, null)
      .create()
      .show()
  }

  private fun refreshSwitchStatus() {
    val isOpen = AccessibilityTools.isOpen(this, RedPacketService::class.java.name)
    if (isOpen) {
      // 打开的
      binding.ivStatus.isVisible = false
      if (statusAnimator.isRunning) {
        statusAnimator.cancel()
      }
    } else {
      // 未打开
      binding.ivStatus.isVisible = true
      if (!userCancel) {
        statusAnimator.start()
      }
    }
  }

  override fun onResume() {
    super.onResume()
    refreshSwitchStatus()
  }

  override fun onStop() {
    super.onStop()
    if (statusAnimator.isRunning) {
      statusAnimator.cancel()
    }
  }

}