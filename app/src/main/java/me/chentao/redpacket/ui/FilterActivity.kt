package me.chentao.redpacket.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.chentao.redpacket.R
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivityFilterBinding
import me.chentao.redpacket.databinding.DialogEditableBinding
import me.chentao.redpacket.utils.KVStore
import me.chentao.redpacket.utils.dp
import me.chentao.redpacket.utils.filterWordsList
import timber.log.Timber

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

  private lateinit var realKeywords: MutableList<String>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.ivBack.setOnClickListener { finish() }

    parseFilterWords()

    binding.btnAdd.setOnClickListener {
      showAddDialog()
    }

    binding.ivMore.setOnClickListener { showRules() }
    binding.empty.setOnClickListener { showRules() }
  }

  private fun showRules() {
    MaterialAlertDialogBuilder(this)
      .setTitle(R.string.filter_rules)
      .setMessage(R.string.filter_description)
      .setPositiveButton(R.string.i_know, null)
      .create()
      .show()
  }

  private fun showAddDialog() {
    val inflate = DialogEditableBinding.inflate(layoutInflater)
    MaterialAlertDialogBuilder(this)
      .setTitle(getString(R.string.add_keyword))
      .setMessage(getString(R.string.keyword_hint))
      .setView(inflate.root)
      .setPositiveButton(getString(R.string.confirm)) { _, _ ->
        val keyword = inflate.editText.text.toString().trim().replace(",", "")
        Timber.d("输入的关键字 -> $keyword")

        if (KVStore.filterWords.contains(keyword)) {
          return@setPositiveButton
        }

        // 添加关键字
        realKeywords.add(keyword)
        addTag(keyword)

        KVStore.filterWords = realKeywords.joinToString(",")

        refreshEmptyUI()
      }
      .setNegativeButton(getString(R.string.cancel), null)
      .create()
      .show()
  }

  private fun parseFilterWords() {
    realKeywords = KVStore.filterWordsList().toMutableList()
    for (keyword in realKeywords) {
      addTag(keyword)
    }
    refreshEmptyUI()
  }

  private fun refreshEmptyUI() {
    binding.empty.isVisible = realKeywords.isEmpty()
  }

  private fun addTag(keyword: String) {
    if (keyword.isNotEmpty()) {
      binding.flexBoxLayout.addView(genTag(keyword), genLayoutParams())
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
    textView.setTextColor(ContextCompat.getColorStateList(this, R.color.filter_tag_clor))

    textView.setOnLongClickListener {
      // 删除关键字
      realKeywords.remove(text)
      binding.flexBoxLayout.removeView(textView)
      KVStore.filterWords = realKeywords.joinToString(",")

      refreshEmptyUI()

      true
    }

    return textView
  }

}