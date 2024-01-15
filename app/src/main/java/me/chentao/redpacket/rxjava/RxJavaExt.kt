package me.chentao.redpacket.rxjava

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * create by chentao on 2024-01-15.
 */

val ioThread = Schedulers.io()
val uiThread = AndroidSchedulers.mainThread()

fun <T : Any> Observable<T>.ioToUiThread(): Observable<T> {
  return this.subscribeOn(ioThread)
    .observeOn(uiThread)
}