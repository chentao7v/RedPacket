package me.chentao.redpacket.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * create by chentao on 2024-01-15.
 */
class ApiClient {

  private val okhttp: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .connectTimeout(30L, TimeUnit.SECONDS)
      .readTimeout(30L, TimeUnit.SECONDS)
      .writeTimeout(30L, TimeUnit.SECONDS)
      .build()
  }

  private val pgy: Retrofit by lazy {
    Retrofit.Builder()
      .baseUrl("https://www.pgyer.com/apiv2/")
      .client(okhttp)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  fun <T : Any> createPgyerService(clazz: Class<T>): T {
    return pgy.create(clazz)
  }


}