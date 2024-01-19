package me.chentao.redpacket.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.R
import me.chentao.redpacket.data.bean.PgyerResponse
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

  companion object {
    private const val NEW_APK_NAME = "new_version.apk"
  }

  private val pgyerRepo = PgyerRepository()
  private val fileRepo = FileRepository()

  fun check(context: Activity) {
    pgyerRepo.checkUpdate()
      .ioToUiThread()
      .safeSubscribe(object : SimpleObserver<PgyerResponse<PgyerUpdateInfo>>() {
        override fun onError(e: Throwable) {
          Timber.e(e, e.message)
          showToast(getStringRes(R.string.api_error))
        }

        override fun onNext(t: PgyerResponse<PgyerUpdateInfo>) {
          val info = t.data
          if (info == null || !info.buildHaveNewVersion) {
            showToast(getStringRes(R.string.already_new_version))
            return
          }

          showUpdateInfoDialog(context, info)
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
    // 判定 APK 是否已经下载过
    val downloadDirectory = getDownloadDirectory()
    val apkFile = File(downloadDirectory, NEW_APK_NAME)
    if (isApkOk(apkFile)) {
      Timber.d("无需下载...")
      showInstallDialog(context, apkFile)
      return
    }
    Timber.d("分析已下载的 apk 后，决定从网络下载")

    showToast(getStringRes(R.string.download_apk_running))
    Observable.just(info)
      .flatMap { _ -> fileRepo.download(info.downloadURL ?: "", apkFile) }
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

          showInstallDialog(context, t)
        }
      })
  }

  private fun isApkOk(file: File): Boolean {
    if (!file.exists() || file.length() == 0L) {
      Timber.w("文件不存在... --> ${file.absolutePath}")
      return false
    }

    val pm = app.packageManager
    val info = pm.getPackageArchiveInfo(file.toString(), PackageManager.GET_ACTIVITIES) ?: return false
    // 服务器给的 apk 信息
    // 包名
    val targetPackageName = info.packageName
    // 版本号
    val targetVersionName = info.versionName
    val targetVersionCode = info.versionCode
    Timber.d("分析本地已下载的 apk， 新的 apk 信息 packageName=$targetPackageName,versionName=$targetVersionName,versionCode:$targetVersionCode")

    // 当前 apk 的信息
    val myPackageName = app.packageName
    val myVersionCode = appVersionCode

    if (targetPackageName != myPackageName) {
      Timber.d("package-name 校验不通过")
      return false
    }

    if (myVersionCode >= targetVersionCode) {
      Timber.d("当前版本号：$myVersionCode 大于等于目标版本号：${targetVersionCode}")
      return false
    }

    return true
  }

  private fun showInstallDialog(context: Activity, apkFile: File) {
    val dialog = MaterialAlertDialogBuilder(context)
      .setTitle(getStringRes(R.string.install_title))
      .setCancelable(false)
      .setMessage(getStringRes(R.string.click_to_install))
      .setPositiveButton(getStringRes(R.string.install_now)) { _, _ ->
        requestInstall(context, apkFile)
      }
      .create()

    dialog.show()
    dialog.setCanceledOnTouchOutside(false)
  }

  private fun requestInstall(context: Activity, apkFile: File) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val pm: PackageManager = app.packageManager
      // 返回用户是否授予了安装apk的权限
      if (pm.canRequestPackageInstalls()) {
        installApk(context, apkFile)
      } else {
        //跳转到该应用的安装应用的权限页面
        val packageURI = Uri.parse("package:" + app.packageName)
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
        context.startActivity(intent)
      }
    } else {
      installApk(context, apkFile)
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
    Timber.d("新下载的 apk 信息 packageName=$targetPackageName,versionName=$targetVersionName,versionCode:$targetVersionCode")

    // 当前 apk 的信息
    val myPackageName = app.packageName

    if (targetPackageName != myPackageName) {
      Timber.d("package-name 校验不通过")
      return false
    }
    return true
  }

}