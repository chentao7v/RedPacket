package me.chentao.redpacket.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.core.view.isVisible
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivityMainBinding
import me.chentao.redpacket.service.RedPacketService
import me.chentao.redpacket.utils.AccessibilityTools

class MainActivity : BaseActivity<ActivityMainBinding>() {

  override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

  private val statusAnimator: ObjectAnimator by lazy {
    ObjectAnimator.ofFloat(binding.ivStatus, "alpha", 0f, 1f).apply {
      duration = 1000
      repeatCount = ValueAnimator.INFINITE
      repeatMode = ValueAnimator.REVERSE
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.robot.setOnClickListener { AccessibilityTools.gotoSettingsUI(this) }
    binding.settings.setOnClickListener { SettingsActivity.launch(this) }
    binding.ivStatus.setOnClickListener { }
  }

  private fun refreshSwitchStatus() {
    val isOpen = AccessibilityTools.isOpen(this, RedPacketService::class.java.name)
    if (isOpen) {
      binding.ivStatus.isVisible = false
      statusAnimator.cancel()
    } else {
      binding.ivStatus.isVisible = true
      statusAnimator.start()
    }
  }

  override fun onResume() {
    super.onResume()
    refreshSwitchStatus()
  }

  override fun onStop() {
    super.onStop()
    statusAnimator.cancel()
  }

}