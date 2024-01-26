package me.chentao.redpacket

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport
import me.chentao.redpacket.notify.initChannelIfNecessary
import me.chentao.redpacket.utils.app
import timber.log.Timber

/**
 * create by chentao on 2023-12-27.
 */
class App : Application() {

  companion object {

//    init {
//      //设置全局的Header构建器
//
//      //设置全局的Header构建器
//      SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
//        layout.setPrimaryColorsId(R.color.red_1)
//        MaterialHeaderCompat(context).apply {
//          setColorSchemeResources(R.color.red_1)
//        }
//      }
//      //设置全局的Footer构建器
//      SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
//        layout.setPrimaryColorsId(R.color.red_1)
//        ClassicsFooter(context)
//      }
//    }

  }

  override fun onCreate() {
    super.onCreate()
    app = this

    Timber.plant(Timber.DebugTree())
    CrashReport.initCrashReport(this, "38b219bf57", false);

    initChannelIfNecessary()
  }
}