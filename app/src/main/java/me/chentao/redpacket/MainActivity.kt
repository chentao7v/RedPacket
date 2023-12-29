package me.chentao.redpacket

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.chentao.redpacket.service.RedPacketService
import me.chentao.redpacket.utils.AccessibilityUtils

class MainActivity : AppCompatActivity() {

  private lateinit var text: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    text = findViewById(R.id.tv_text)

    refreshSwitchStatus()
  }

  private fun refreshSwitchStatus() {
    val isOpen = AccessibilityUtils.isAccessibilitySettingsOn(this, RedPacketService::class.java.name)
    text.text = "服务状态：" + if (isOpen) "开" else "关"
  }

  fun openRedPacketService(view: View) {
    AccessibilityUtils.jumpToSettingPage(this)
  }

  override fun onResume() {
    super.onResume()
    refreshSwitchStatus()
  }

}