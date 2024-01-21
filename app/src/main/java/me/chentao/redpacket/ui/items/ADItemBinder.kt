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
import me.chentao.redpacket.utils.ADJumper

/**
 * create by chentao on 2024-01-18.
 */
class ADItemBinder(
  private val click: (Int) -> Unit
) : ItemViewBinder<ADItem, ADItemBinder.ViewHolder>() {

  private val repo = ADRepository()

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
    return ViewHolder(ItemAdBinding.inflate(inflater, parent, false), repo, click)
  }

  override fun onBindViewHolder(holder: ViewHolder, item: ADItem) {
    holder.bind(item)
  }


  class ViewHolder(
    private val binding: ItemAdBinding,
    repo: ADRepository, click: (Int) -> Unit
  ) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: ADItem

    init {
      binding.root.setOnClickListener {
        // 广告跳转
        // 更新广告数量
        repo.updateADViewCount(item.id)
          .safeSubscribe(SimpleObserver())

        // 跳转广告
        ADJumper.launch(binding.root.context, item)

        // 广告次数增加
        item.viewCount++
        click.invoke(bindingAdapterPosition)
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