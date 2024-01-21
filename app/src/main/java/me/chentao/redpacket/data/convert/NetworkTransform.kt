package me.chentao.redpacket.data.convert

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.ObservableTransformer
import me.chentao.redpacket.data.bean.BaseResponse
import me.chentao.redpacket.data.bean.DataListResponse
import me.chentao.redpacket.data.bean.DataResponse
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

  companion object {

    fun ofMini(): NetworkTransform<BaseResponse> {
      return NetworkTransform(Types.ofDefault())
    }

    fun <T : Any> ofData(clazz: Class<T>): NetworkTransform<DataResponse<T>> {
      return NetworkTransform(Types.ofDefaultData(clazz))
    }

    fun <T : Any> ofDataList(clazz: Class<T>): NetworkTransform<DataListResponse<T>> {
      return NetworkTransform(Types.ofDefaultDataList(clazz));
    }

  }

}