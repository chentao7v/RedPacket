package me.chentao.redpacket.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * create by chentao on 2024-01-15.
 */
object ApiClient {

  private val okhttp: OkHttpClient by lazy {

    val httpLog = HttpLoggingInterceptor { msg ->
      Timber.tag("http").d(msg)
    }
    httpLog.level = HttpLoggingInterceptor.Level.BODY

    OkHttpClient.Builder()
      .connectTimeout(30L, TimeUnit.SECONDS)
      .readTimeout(30L, TimeUnit.SECONDS)
      .writeTimeout(30L, TimeUnit.SECONDS)
      .addInterceptor(httpLog)
      .build()
  }

  private val default: Retrofit by lazy {
    Retrofit.Builder()
      .baseUrl("http://zybkkf.natappfree.cc/")
      .client(okhttp)
      .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  private val pgy: Retrofit by lazy {
    Retrofit.Builder()
      .baseUrl("https://www.pgyer.com/apiv2/")
      .client(okhttp)
      .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  fun <T : Any> createPgyerService(clazz: Class<T>): T {
    return pgy.create(clazz)
  }

  fun <T : Any> createDefaultService(clazz: Class<T>): T {
    return default.create(clazz)
  }



}