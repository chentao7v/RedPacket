package me.chentao.redpacket.processor

import timber.log.Timber

/**
 * create by chentao on 2023-12-29.
 */
object Filter {

  fun filter(text: String): Boolean {
    Timber.d("过滤器入参：$text")
    return true
  }

}