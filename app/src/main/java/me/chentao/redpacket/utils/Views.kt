package me.chentao.redpacket.utils

import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * create by chentao on 2024-01-18.
 */

fun SmartRefreshLayout.safeFinishLoad(isRefresh: Boolean) {
  post {
    if (isRefresh) {
      finishRefresh()
    } else {
      finishLoadMore()
    }
  }
}