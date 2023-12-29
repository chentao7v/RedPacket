package me.chentao.redpacket.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import me.chentao.redpacket.R
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivitySettingsBinding

/**
 * create by chentao on 2023-12-29.
 */
class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

  companion object {

    fun launch(context: Context) {
      val intent = Intent(context, SettingsActivity::class.java)
      context.startActivity(intent)
    }

  }

  override fun getViewBinding() = ActivitySettingsBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

}