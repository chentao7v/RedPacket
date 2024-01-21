package me.chentao.redpacket.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import androidx.lifecycle.Lifecycle
import autodispose2.androidx.lifecycle.autoDispose
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.databinding.ActivityRichTextBinding
import me.chentao.redpacket.rxjava.SimpleObserver
import me.chentao.redpacket.rxjava.ioToUiThread
import me.chentao.redpacket.utils.dp
import me.chentao.redpacket.utils.screenWidth
import me.chentao.redpacket.utils.toHttps

/**
 * 富文本页面
 */
class RichTextActivity : BaseActivity<ActivityRichTextBinding>() {

  companion object {

    private const val EXTRA_TITLE = "extra_title"
    private const val EXTRA_RICH_TEXT = "extra_rich_text"

    fun launch(context: Context, title: String, richText: CharSequence) {
      val intent = Intent(context, RichTextActivity::class.java)
      intent.putExtra(EXTRA_TITLE, title)
      intent.putExtra(EXTRA_RICH_TEXT, richText)
      context.startActivity(intent)
    }

  }

  override fun getViewBinding(): ActivityRichTextBinding {
    return ActivityRichTextBinding.inflate(layoutInflater)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
    val richText = intent.getStringExtra(EXTRA_RICH_TEXT) ?: ""

    binding.tvTitle.text = title

    Observable
      .fromCallable { parseToSpannable(richText) }
      .ioToUiThread()
      .autoDispose(this, Lifecycle.Event.ON_DESTROY)
      .subscribe(object : SimpleObserver<Spanned>() {
        override fun onNext(t: Spanned) {
          binding.tvContent.movementMethod = LinkMovementMethod.getInstance()
          binding.tvContent.text = t
        }
      })
  }

  private fun parseToSpannable(richText: String): Spanned {

    val imageGetter = Html.ImageGetter { source ->
      val url = source?.toHttps()

      val bitmap = Glide.with(this).asBitmap().load(url).submit().get()
      val drawable: Drawable = BitmapDrawable(bitmap)

      val imageWidth = bitmap.width
      val imageHeight = bitmap.height

      val realWidth = screenWidth - 15.dp * 2
      val realHeight = (imageHeight * 1f / imageWidth * realWidth).toInt()

      drawable.setBounds(0, 0, realWidth, realHeight)
      drawable
    }

    return Html.fromHtml(richText, imageGetter, null)
  }

}