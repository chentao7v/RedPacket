package me.chentao.redpacket

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.chentao.redpacket.service.RedPacketService
import me.chentao.redpacket.utils.AccessibilityTools

class MainActivity : AppCompatActivity() {

  private lateinit var text: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    text = findViewById(R.id.tv_text)

    refreshSwitchStatus()
  }

  private fun refreshSwitchStatus() {
    val isOpen = AccessibilityTools.isAccessibilityOpen(this, RedPacketService::class.java.name)
    text.text = "服务状态：" + if (isOpen) "开" else "关"
  }

  fun openRedPacketService(view: View) {
    AccessibilityTools.launchSettings(this)
  }

  override fun onResume() {
    super.onResume()
    refreshSwitchStatus()
  }

}