package me.chentao.redpacket.ui

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.chentao.redpacket.R
import me.chentao.redpacket.base.BaseActivity
import me.chentao.redpacket.data.bean.ADItem
import me.chentao.redpacket.databinding.ActivityMainBinding
import me.chentao.redpacket.ui.items.ADItemBinder
import me.chentao.redpacket.ui.items.SpaceItemDecoration
import me.chentao.redpacket.utils.AccessibilityTools
import me.chentao.redpacket.utils.StatusAlphaAnimator
import me.chentao.redpacket.utils.copyToClipboard
import me.chentao.redpacket.utils.dp
import me.chentao.redpacket.utils.postDelay
import me.chentao.redpacket.utils.safeAutoRefresh
import me.chentao.redpacket.utils.safeFinish
import me.chentao.redpacket.utils.showToast
import java.util.Random

class MainActivity : BaseActivity<ActivityMainBinding>() {

  override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

  private lateinit var statusAnimator: StatusAlphaAnimator

  private lateinit var adapter: MultiTypeAdapter
  private lateinit var items: MutableList<ADItem>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.robot.setOnClickListener { AccessibilityTools.gotoSettingsUI(this) }
    binding.settings.setOnClickListener { SettingsActivity.launch(this) }
    binding.ivStatus.setOnClickListener { showRobotDialog() }
    binding.btnContactUs.setOnClickListener { contactUs() }

    statusAnimator = StatusAlphaAnimator(binding.ivStatus)

    initRecyclerView()
    initRefreshAndLoadMore()
  }

  private fun contactUs() {
    MaterialAlertDialogBuilder(this)
      .setTitle(R.string.contact_us)
      .setMessage(getString(R.string.how_to_contact_us))
      .setPositiveButton(R.string.get_contact) { _, _ ->
        gotoWechat()
      }
      .create()
      .show()
  }

  private fun gotoWechat() {
    "kingshow321".copyToClipboard()
    val intent = Intent()
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")

    val resolved = intent.resolveActivity(packageManager)
    if (resolved != null) {
      showToast(getString(R.string.contact_us_with_wechat))
      startActivity(intent)
    } else {
      showToast(getString(R.string.contact_us_without_wechat))
    }
  }


  private fun initRefreshAndLoadMore() {
    binding.refresher.setOnRefreshListener {
      postDelay(1000) {
        mockData(true)
        binding.refresher.safeFinish(true)
      }
    }

    binding.refresher.setOnLoadMoreListener {
      postDelay(1000) {
        mockData(false)
        binding.refresher.safeFinish(false)
      }
    }

    binding.refresher.safeAutoRefresh()
  }

  private fun initRecyclerView() {
    adapter = MultiTypeAdapter()
    adapter.register(ADItemBinder())

    items = ArrayList()

    binding.recyclerView.adapter = adapter
    binding.recyclerView.addItemDecoration(SpaceItemDecoration(6.dp))

    val layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    layout.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
    binding.recyclerView.layoutManager = layout
  }

  private fun mockData(isRefresh: Boolean) {
    val items = ArrayList<ADItem>()
    val offset = if (isRefresh) {
      0
    } else {
      this.items.size
    }
    for (i in 0..100) {
      val adItem = ADItem()
      adItem.height = getRandomHeight()
      adItem.position = i + offset
      items.add(adItem)
    }

    addItems(isRefresh, items)
  }

  private fun getRandomHeight(): Int {
    return Random().nextInt(300) + 300
  }

  private fun addItems(isRefresh: Boolean, items: List<ADItem>) {
    if (isRefresh) {
      this.items.clear()
      this.items.addAll(items)
      adapter.items = this.items
      adapter.notifyDataSetChanged()
    } else {
      val oldSize = this.items.size
      val insertTotal = items.size
      this.items.addAll(items)
      adapter.items = this.items
      adapter.notifyItemRangeInserted(oldSize, insertTotal)
    }
  }

  private fun showRobotDialog() {
    statusAnimator.cancelManual()

    MaterialAlertDialogBuilder(this)
      .setTitle(R.string.alert_default_title)
      .setMessage(R.string.robot_close)
      .setPositiveButton(R.string.i_know, null)
      .create()
      .show()
  }

  override fun onResume() {
    super.onResume()
    statusAnimator.refreshStatus()
  }

  override fun onStop() {
    super.onStop()

    statusAnimator.cancel()
  }

}