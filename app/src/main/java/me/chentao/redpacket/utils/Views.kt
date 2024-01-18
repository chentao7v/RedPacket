package me.chentao.redpacket.utils

import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * create by chentao on 2024-01-18.
 */

fun SmartRefreshLayout.safeFinish(isRefresh: Boolean) {
  post {
    if (isRefresh) {
      finishRefresh()
    } else {
      finishLoadMore()
    }
  }
}

fun SmartRefreshLayout.safeAutoRefresh() {
  post { autoRefresh() }
}