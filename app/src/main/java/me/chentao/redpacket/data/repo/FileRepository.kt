package me.chentao.redpacket.data.repo

import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.data.ApiClient
import me.chentao.redpacket.data.api.FileApi
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


/**
 * create by chentao on 2023-11-22.
 */
class FileRepository {

  private val api: FileApi = ApiClient.createPgyerService(FileApi::class.java)

  /**
   * 下载文件
   */
  fun download(url: String, file: File): Observable<File> {
    return api.download(url)
      .map { responseBody ->
        val inputStream: InputStream = responseBody.byteStream()
        if (file.exists()) {
          file.delete()
        }
        val fos = FileOutputStream(file)
        var len: Int
        val buffer = ByteArray(4096)

        while (inputStream.read(buffer).also { len = it } != -1) {
          fos.write(buffer, 0, len)
        }
        fos.close()
        inputStream.close()
        file
      }
  }
}