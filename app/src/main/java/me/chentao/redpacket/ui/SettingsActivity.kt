package me.chentao.redpacket.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.chentao.redpacket.R
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivitySettingsBinding
import me.chentao.redpacket.notify.foregroundChannel
import me.chentao.redpacket.notify.gotoNotifySettings
import me.chentao.redpacket.notify.judgeNotificationPermission
import me.chentao.redpacket.service.RedPacketService
import me.chentao.redpacket.utils.AccessibilityTools
import me.chentao.redpacket.utils.KVStore
import me.chentao.redpacket.utils.hideFromRecentTasks
import me.chentao.redpacket.utils.toAppSettings
import me.chentao.redpacket.utils.toLauncher


/**
 * create by chentao on 2023-12-29.
 */
class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

  companion object {

    private const val REQUEST_POST_NOTIFICATIONS = 0X100

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
    binding.hide.setOnClickListener { switchHide() }
    binding.lock.setOnClickListener { showAlert(getString(R.string.lock_hint)) }
    binding.battery.setOnClickListener { showAlert(getString(R.string.battery_hint)) { toAppSettings(this) } }

    refreshNotificationUI()
    refreshConversationListUI()
    refreshMyselfUI()
    refreshHideUI()
  }

  private fun switchHide() {
    val isChecked = binding.cbHide.isChecked
    val hide = !isChecked
    hideFromRecentTasks(this, hide)
    KVStore.hide = hide
    refreshHideUI()

    if (hide) {
      // 跳转桌面
      toLauncher(this)
    }
  }

  private fun refreshHideUI() {
    binding.cbHide.isChecked = KVStore.hide
  }

  private fun switchForeground() {
    if (!AccessibilityTools.isOpen(this, RedPacketService::class.java.name)) {
      showAlert(getString(R.string.open_robot_first))
      return
    }

    // 当前状态
    val isChecked = binding.cbForeground.isChecked

    if (!judgeNotificationPermission(foregroundChannel.id)) {
      showAlert(getString(R.string.open_notification_first, foregroundChannel.name)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
          && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)
        ) {
          // 请求通知权限
          ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_POST_NOTIFICATIONS)
        } else {
          // 直接去通知设置页面
          gotoNotifySettings(this, foregroundChannel.id)
        }
      }
      return
    }

    // 未开启 -> 开启前台服务
    RedPacketService.startForeground(this, !isChecked)

    // UI 刷新
    KVStore.foreground = !isChecked
    refreshForegroundUI()
  }

  private fun refreshForegroundUI() {
    binding.cbForeground.isChecked = KVStore.foreground && judgeNotificationPermission(foregroundChannel.id)
  }


  private fun switchMySelf() {
    val isChecked = binding.cbMyself.isChecked
    KVStore.openMySelf = !isChecked
    refreshMyselfUI()
  }

  private fun refreshMyselfUI() {
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

  private fun showAlert(msg: CharSequence, clickBlock: (() -> Unit)? = null) {
    val dialog = MaterialAlertDialogBuilder(this)
      .setTitle(getString(R.string.alert_default_title))
      .setMessage(msg)
      .setCancelable(false)
      .setPositiveButton(getString(R.string.i_know)) { _, _ ->
        clickBlock?.invoke()
      }
      .create()

    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
  }

}