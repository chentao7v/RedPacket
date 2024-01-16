package me.chentao.redpacket.data.convert

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody

/**
 * 将 [ResponseBody] 转换为 [String]
 *
 * create by chentao on 2022-10-18.
 */
class ToStringFunction : io.reactivex.rxjava3.functions.Function<ResponseBody, Observable<String>> {

  override fun apply(responseBody: ResponseBody): Observable<String> {
    return try {
      Observable.just(responseBody.string())
    } catch (e: Exception) {
      Observable.error(e)
    }
  }
}