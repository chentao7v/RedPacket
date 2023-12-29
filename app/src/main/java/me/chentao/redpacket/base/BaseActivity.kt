package me.chentao.redpacket.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * create by chentao on 2023-12-29.
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

  protected lateinit var binding: VB

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = getViewBinding()
    setContentView(binding.root)
  }

  abstract fun getViewBinding(): VB

}