package me.chentao.redpacket.ui

import android.os.Bundle
import me.chentao.redpacket.R
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivityMainBinding
import me.chentao.redpacket.service.RedPacketService
import me.chentao.redpacket.utils.AccessibilityTools

class MainActivity : BaseActivity<ActivityMainBinding>() {

  override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.service.setOnClickListener { AccessibilityTools.gotoSettingsUI(this) }
    binding.settings.setOnClickListener { SettingsActivity.launch(this) }
  }

  private fun refreshSwitchStatus() {
    val isOpen = AccessibilityTools.isOpen(this, RedPacketService::class.java.name)
    val status = if (isOpen) {
      binding.tvStatus.setTextColor(getColor(R.color.red_1))
      getString(R.string.robot_open)
    } else {
      binding.tvStatus.setTextColor(getColor(R.color.black_1))
      getString(R.string.robot_close)
    }
    binding.tvStatus.text = status
  }

  override fun onResume() {
    super.onResume()
    refreshSwitchStatus()
  }

}