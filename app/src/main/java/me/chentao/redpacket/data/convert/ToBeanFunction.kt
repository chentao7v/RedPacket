package me.chentao.redpacket.data.convert

import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.utils.JSONMaker
import java.lang.reflect.Type

/**
 * 将 json 转换为 Bean
 *
 * create by chentao on 2022-10-18.
 */
class ToBeanFunction<T : Any>(
  private val type: Type
) : io.reactivex.rxjava3.functions.Function<String, Observable<T>> {

  override fun apply(json: String): Observable<T> {
    return try {
      val response = JSONMaker.fromJson<T>(json, type)
      Observable.just(response)
    } catch (e: java.lang.Exception) {
      Observable.error(e)
    }
  }

}