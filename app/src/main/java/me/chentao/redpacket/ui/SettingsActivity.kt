package me.chentao.redpacket.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.chentao.redpacket.R
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
    binding.ivBack.setOnClickListener { finish() }
    binding.filter.setOnClickListener { FilterActivity.launch(this) }

    binding.conversations.setOnClickListener { switchConversationList() }
    binding.myself.setOnClickListener { switchMySelf() }
    binding.foreground.setOnClickListener { switchForeground() }

    refreshNotificationUI()
  }

  private fun switchForeground() {
    val isChecked = binding.cbForeground.isChecked



    KVStore.foreground = !isChecked
  }



  private fun switchMySelf() {
    val isChecked = binding.cbMyself.isChecked
    KVStore.openMySelf = !isChecked
    binding.cbMyself.isChecked = KVStore.openMySelf
  }

  private fun switchConversationList() {
    val isChecked = binding.cbConversationList.isChecked
    if (!isChecked && !KVStore.conversationHint) {
      showAlert(getString(R.string.conversation_list_hint))
      KVStore.conversationHint = true
    }

    KVStore.conversationList = !isChecked
    refreshConversationListUI()

  }

  private fun refreshConversationListUI() {
    binding.cbConversationList.isChecked = KVStore.conversationList
  }

  private fun switchNotification() {
    val isChecked = binding.cbNotification.isChecked
    if (!isChecked && !KVStore.notificationHint) {
      showAlert(getString(R.string.notification_hint))
      KVStore.notificationHint = true
    }

    KVStore.notification = !isChecked
    refreshNotificationUI()
  }

  private fun refreshNotificationUI() {
    binding.cbNotification.isChecked = KVStore.notification
  }

  private fun showAlert(msg: CharSequence) {
    val dialog = MaterialAlertDialogBuilder(this)
      .setTitle(getString(R.string.alert_default_title))
      .setMessage(msg)
      .setCancelable(false)
      .setPositiveButton(getString(R.string.i_know), null)
      .create()

    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
  }

}