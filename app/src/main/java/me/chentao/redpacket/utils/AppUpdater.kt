package me.chentao.redpacket.utils

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.R
import me.chentao.redpacket.data.bean.PgyerUpdateInfo
import me.chentao.redpacket.data.repo.FileRepository
import me.chentao.redpacket.data.repo.PgyerRepository
import me.chentao.redpacket.rxjava.SimpleObserver
import me.chentao.redpacket.rxjava.ioToUiThread
import me.chentao.redpacket.service.RedPacketService
import timber.log.Timber
import java.io.File

/**
 * App 升级处理
 *
 * create by chentao on 2024-01-15.
 */
class AppUpdater {

  private val pgyerRepo = PgyerRepository()
  private val fileRepo = FileRepository()

  fun check(context: Activity) {
    pgyerRepo.checkUpdate()
      .filter { data -> data.buildHaveNewVersion }
      .safeSubscribe(object : SimpleObserver<PgyerUpdateInfo>() {
        override fun onError(e: Throwable) {
          showToast(getStringRes(R.string.api_error))
        }

        override fun onNext(t: PgyerUpdateInfo) {
          showUpdateInfoDialog(context, t)
        }
      })
  }

  private fun showUpdateInfoDialog(context: Activity, info: PgyerUpdateInfo) {
    // 需要强制升级
    val needForceUpdate = info.needForceUpdate

    val builder = MaterialAlertDialogBuilder(context)
      .setTitle(getStringRes(R.string.found_new_version, info.buildVersion))
      .setMessage(info.buildUpdateDescription ?: "")
      .setCancelable(needForceUpdate)
      .setPositiveButton(getStringRes(R.string.app_update_now)) { _, _ ->
        downloadNewApk(context, info)
      }

    val negativeText = if (needForceUpdate) {
      getStringRes(R.string.exit_app)
    } else {
      getStringRes(R.string.cancel_update_app)
    }

    builder.setNegativeButton(negativeText) { _, _ ->
      if (needForceUpdate) {
        // 强制更新点击取消时
        RedPacketService.startForeground(context, false)
        RedPacketService.stop(context)

        KVStore.requireNewVersion = true
        // 退出 APP
        System.exit(0)
      }
    }


    val dialog = builder.create()

    dialog.setCanceledOnTouchOutside(needForceUpdate)
    dialog.show()
  }

  private fun downloadNewApk(context: Activity, info: PgyerUpdateInfo) {
    Observable.just(info)
      .flatMap { _ -> fileRepo.download(info.downloadURL ?: "", "new_version.apk") }
      .ioToUiThread()
      .safeSubscribe(object : SimpleObserver<File>() {
        override fun onError(e: Throwable) {
          showToast(getStringRes(R.string.api_error))
        }

        override fun onNext(t: File) {
          if (!analysisApkOk(t)) {
            showToast(getStringRes(R.string.app_info_error))
            return
          }

          requestInstall(context, t)
        }
      })

  }

  private fun requestInstall(context: Activity, apkFile: File) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.REQUEST_INSTALL_PACKAGES) == PackageManager.PERMISSION_GRANTED) {
      installApk(context, apkFile)
    } else if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
      ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.REQUEST_INSTALL_PACKAGES), 1002)
    } else {
      toAppSettings(context)
    }
  }

  private fun installApk(context: Context, apkFile: File) {
    if (apkFile.exists()) {
      Timber.d("开始执行安装 apk 操作")
      val intent = Intent(Intent.ACTION_VIEW)
      intent.setDataAndType(apkFile.toSafeUri(), "application/vnd.android.package-archive")
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      try {
        context.startActivity(intent)
      } catch (e: ActivityNotFoundException) {
        Timber.e("安装时，跳转安装页面失败")
      }
    } else {
      Timber.e("安装时，发现文件不存在")
    }

    // 均标识为可以正常使用，下次还会检测更新
    KVStore.requireNewVersion = false
  }


  private fun analysisApkOk(file: File): Boolean {
    if (!file.exists()) {
      return false;
    }
    val pm = app.packageManager
    val info = pm.getPackageArchiveInfo(file.toString(), PackageManager.GET_ACTIVITIES) ?: return false
    // 服务器给的 apk 信息
    // 包名
    val targetPackageName = info.packageName
    // 版本号
    val targetVersionName = info.versionName
    val targetVersionCode = info.versionCode
    Timber.d("新的 apk 信息 packageName=$targetPackageName,versionName=$targetVersionName,versionCode:$targetVersionCode")

    // 当前 apk 的信息
    val myPackageName = app.packageName

    if (targetPackageName != myPackageName) {
      Timber.d("package-name 校验不通过")
      return false
    }
    return true
  }

}