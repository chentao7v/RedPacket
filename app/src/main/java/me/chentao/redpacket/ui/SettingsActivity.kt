package me.chentao.redpacket.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivitySettingsBinding
import me.chentao.redpacket.utils.KVStore

/**
 * create by chentao on 2023-12-29.
 */
class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

  companion object {

    fun launch(context: Context) {
      val intent = Intent(context, SettingsActivity::class.java)
      context.startActivity(intent)
    }

  }

  override fun getViewBinding() = ActivitySettingsBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.notification.setOnClickListener { switchNotification() }

    refreshNotificationUI()
  }

  private fun switchNotification() {
    val isChecked = binding.cbNotification.isChecked
    KVStore.notification = !isChecked
    refreshNotificationUI()
  }

  private fun refreshNotificationUI() {
    binding.cbNotification.isChecked = KVStore.notification
  }

}