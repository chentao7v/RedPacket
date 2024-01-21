package me.chentao.redpacket.rxjava

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

/**
 * create by chentao on 2024-01-15.
 */
open class SimpleObserver<T : Any> : Observer<T> {
  override fun onSubscribe(d: Disposable) {

  }

  override fun onError(e: Throwable) {
    Timber.e(e)
  }

  override fun onComplete() {

  }

  override fun onNext(t: T) {

  }
}