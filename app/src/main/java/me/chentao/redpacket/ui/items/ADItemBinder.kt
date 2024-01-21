package me.chentao.redpacket.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import me.chentao.redpacket.data.bean.ADItem
import me.chentao.redpacket.data.repo.ADRepository
import me.chentao.redpacket.databinding.ItemAdBinding
import me.chentao.redpacket.rxjava.SimpleObserver

/**
 * create by chentao on 2024-01-18.
 */
class ADItemBinder : ItemViewBinder<ADItem, ADItemBinder.ViewHolder>() {

  private val repo = ADRepository()

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
    return ViewHolder(ItemAdBinding.inflate(inflater, parent, false), repo)
  }

  override fun onBindViewHolder(holder: ViewHolder, item: ADItem) {
    holder.bind(item)
  }


  class ViewHolder(private val binding: ItemAdBinding, repo: ADRepository) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: ADItem

    init {
      binding.root.setOnClickListener {
        // 广告跳转
        // 更新广告数量
        repo.updateADViewCount(item.id)
          .safeSubscribe(SimpleObserver())
      }
    }

    fun bind(item: ADItem) {
      this.item = item

      Glide.with(binding.ivAvatar)
        .load(item.image)
        .into(binding.ivAvatar)

      binding.tvTitle.text = item.title
      binding.tvViewCount.text = "${item.viewCount}"

      val params = binding.ivAvatar.layoutParams as ViewGroup.LayoutParams
      params.height = item.height
      binding.ivAvatar.layoutParams = params
    }

  }

}