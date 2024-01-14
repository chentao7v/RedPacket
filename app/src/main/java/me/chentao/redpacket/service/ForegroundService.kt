package me.chentao.redpacket.service

import android.app.Service
import android.content.Intent

/**
 * 前台服务，显示无障碍服务状态
 * <br>
 * create by chentao on 2024-01-14.
 */
class ForegroundService : Service() {

  override fun onBind(intent: Intent?) = null

  override fun onCreate() {
    super.onCreate()


  }
}