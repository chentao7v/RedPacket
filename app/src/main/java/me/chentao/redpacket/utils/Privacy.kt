package me.chentao.redpacket.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.chentao7v.span.Configs
import io.github.chentao7v.span.Spans
import me.chentao.redpacket.R

/**
 * create by chentao on 2024-01-20.
 */
object Privacy {

  fun show(context: Context) {
    val default = Configs.text()
    val bold = Configs.text().bold()

    val buglyUrl = "https://privacy.qq.com/document/preview/fc748b3d96224fdb825ea79e132c1a56"


    val msg = Spans.pipeline()
      .add("让红包飞", bold)
      .add("是一款帮助老人、残疾人抢红包的工具。致力于解放双手，自动抢红包，让特殊人群也能享受到抢红包的乐趣~\n\n", default)
      .add("让红包飞不需要您登录任何账号即可使用，因此不会收集您的个人隐私信息。\n\n", default)
      .add("使用到的权限：\n", bold)
      .add(" - 无障碍：", bold)
      .add("用于处理与红包相关的交互。我们不会用它来窃取您的任何私人信息，请放心使用。\n", default)
      .add(" - 前台服务：", bold)
      .add("用于确保小助手后台运行。若您不想小助手后台运行，则不需要改权限。\n", default)
      .add(" - 发送通知：", bold)
      .add("用于在通知栏中展示小助手的运行状态\n", default)
      .add(" - 安装应用：", bold)
      .add("用于升级「让红包飞」。\n\n", default)
      .add("使用到的第三方 SDK：\n", bold)
      .add(" - Bugly：", bold)
      .add("用于收集应用 Crash 信息。", default)
      .add("Bugly 个人信息保护规则：", default)
      .add(buglyUrl, Configs.text().underline()
        .color(app.getColor(R.color.green_1))
        .click {
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse(buglyUrl))
          if (intent.resolveActivity(app.packageManager) != null) {
            context.startActivity(intent)
          } else {
            buglyUrl.copyToClipboard()
            showToast(getStringRes(R.string.copy_to_clipboard))
          }
        })
      .execute()

    val dialog = MaterialAlertDialogBuilder(context)
      .setTitle(app.getString(R.string.setting_privacy_title))
      .setMessage(msg)
      .setPositiveButton(R.string.i_know, null)
      .create()
    dialog.show()

    val msgView = dialog.findViewById<TextView>(android.R.id.message)
    msgView?.isClickable = true
    msgView?.movementMethod = LinkMovementMethod.getInstance()
  }

}