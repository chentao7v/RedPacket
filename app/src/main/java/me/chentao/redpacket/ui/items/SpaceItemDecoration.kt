package me.chentao.redpacket.ui.items

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * create by chentao on 2024-01-18.
 */
class SpaceItemDecoration(
  private val space: Int,
) : ItemDecoration() {

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    outRect.left = space
    outRect.right = space
    outRect.bottom = space
    outRect.top = space
  }

}