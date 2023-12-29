package me.chentao.redpacket.ui

import android.content.Context
import android.content.Intent
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivityFilterBinding

/**
 * create by chentao on 2023-12-29.
 */
class FilterActivity : BaseActivity<ActivityFilterBinding>() {

  companion object {

    fun launch(context: Context) {
      val intent = Intent(context, FilterActivity::class.java)
      context.startActivity(intent)
    }

  }

  override fun getViewBinding() = ActivityFilterBinding.inflate(layoutInflater)
}