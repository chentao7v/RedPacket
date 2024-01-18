package me.chentao.redpacket.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import me.chentao.redpacket.data.bean.ADItem
import me.chentao.redpacket.databinding.ItemAdBinding
import timber.log.Timber

/**
 * create by chentao on 2024-01-18.
 */
class ADItemBinder : ItemViewBinder<ADItem, ADItemBinder.ViewHolder>() {

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
    return ViewHolder(ItemAdBinding.inflate(inflater, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, item: ADItem) {
    holder.bind(item)
  }


  class ViewHolder(private val binding: ItemAdBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ADItem) {
      Glide.with(binding.ivAvatar)
        .load(item.image)
        .into(binding.ivAvatar)

      binding.tvTitle.text = "恰同学少年 ${item.position}"

      val params = binding.ivAvatar.layoutParams as ViewGroup.LayoutParams
      params.height = item.height
      binding.ivAvatar.layoutParams = params
    }

  }

}