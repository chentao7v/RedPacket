package me.chentao.redpacket.data.convert

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.ObservableTransformer
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * 网络转换器。
 * 将 [ResponseBody] 转换为特定类型的 Bean
 *
 * create by chentao on 2022-10-18.
 */
internal class NetworkTransform<R : Any>(
  private val type: Type
) : ObservableTransformer<ResponseBody, R> {

  override fun apply(upstream: Observable<ResponseBody>): ObservableSource<R> {
    return upstream
      .flatMap(ToStringFunction())
      .flatMap(ToBeanFunction(type))
  }
}