package me.chentao.redpacket.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import me.chentao.redpacket.R
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivityFilterBinding
import me.chentao.redpacket.utils.dp

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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.ivBack.setOnClickListener { finish() }

    for (i in 0..30) {
      val tag = genTag("哈哈哈~${i}")
      binding.flexBoxLayout.addView(tag, genLayoutParams())
    }

  }

  private fun genLayoutParams(): FlexboxLayout.LayoutParams {
    val params = FlexboxLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    params.setMargins(5.dp, 5.dp, 5.dp, 5.dp)
    return params
  }

  private fun genTag(text: String): TextView {
    val textView = TextView(this)
    textView.text = text
    textView.isSingleLine = true
    textView.setBackgroundResource(R.drawable.bg_filter_tag)
    textView.setPadding(15.dp, 7.dp, 15.dp, 7.dp)
    textView.setTextColor(ContextCompat.getColorStateList(this,R.color.filter_tag_clor))

    textView.setOnLongClickListener {
      binding.flexBoxLayout.removeView(textView)
      true
    }

    return textView
  }

}