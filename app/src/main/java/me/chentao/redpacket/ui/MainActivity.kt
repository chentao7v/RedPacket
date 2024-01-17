package me.chentao.redpacket.ui

import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.chentao.redpacket.R
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivityMainBinding
import me.chentao.redpacket.service.RedPacketService
import me.chentao.redpacket.utils.AccessibilityTools
import me.chentao.redpacket.utils.StatusAlphaAnimator

class MainActivity : BaseActivity<ActivityMainBinding>() {

  override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

  private lateinit var statusAnimator: StatusAlphaAnimator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.robot.setOnClickListener { AccessibilityTools.gotoSettingsUI(this) }
    binding.settings.setOnClickListener { SettingsActivity.launch(this) }
    binding.ivStatus.setOnClickListener { showRobotDialog() }

    statusAnimator = StatusAlphaAnimator(binding.ivStatus)
  }

  private fun showRobotDialog() {
    statusAnimator.cancelManual()

    MaterialAlertDialogBuilder(this)
      .setTitle(R.string.alert_default_title)
      .setMessage(R.string.robot_close)
      .setPositiveButton(R.string.i_know, null)
      .create()
      .show()
  }

  override fun onResume() {
    super.onResume()
    statusAnimator.refreshStatus()
  }

  override fun onStop() {
    super.onStop()

    statusAnimator.cancel()
  }

}