package me.chentao.redpacket

import android.os.Bundle
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivityMainBinding
import me.chentao.redpacket.service.RedPacketService
import me.chentao.redpacket.utils.AccessibilityTools

class MainActivity : BaseActivity<ActivityMainBinding>() {

  override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.service.setOnClickListener { AccessibilityTools.gotoSettingsUI(this) }
  }

  private fun refreshSwitchStatus() {
    val isOpen = AccessibilityTools.isOpen(this, RedPacketService::class.java.name)
    val status = if (isOpen) getString(R.string.robot_open) else getString(R.string.robot_close)
    binding.tvStatus.text = getString(R.string.robot_status, status)
  }

  override fun onResume() {
    super.onResume()
    refreshSwitchStatus()
  }

}