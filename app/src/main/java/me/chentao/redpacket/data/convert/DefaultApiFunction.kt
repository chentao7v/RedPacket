package me.chentao.redpacket.data.convert

import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.R
import me.chentao.redpacket.data.bean.BaseResponse
import me.chentao.redpacket.utils.getStringRes

/**
 * create by chentao on 2024-01-21.
 */
class DefaultApiFunction<T : BaseResponse> : io.reactivex.rxjava3.functions.Function<T, Observable<T>> {
  override fun apply(t: T): Observable<T> {
    return if (t.code == 200) {
      Observable.just(t)
    } else {
      Observable.error(Exception(t.msg ?: getStringRes(R.string.net_error)))
    }
  }

}