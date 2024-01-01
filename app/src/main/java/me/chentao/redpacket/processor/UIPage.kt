package me.chentao.redpacket.processor

/**
 * create by chentao on 2024-01-01.
 */
interface UIPage {

  companion object {

    const val DEFAULT_PAGE = "LauncherUI"

    /** 新红包，未拆开，红包展示页面，可以点击开；红包已被他人领取 */
    const val PACKET_SHOW_NOT_OPEN = "LuckyMoneyNotHookReceiveUI"

    /** 进入红包详情页前的 UI */
    const val PACKET_OPENED_BEFORE_DETAIL = "LuckyMoneyBeforeDetailUI"

    /** 红包详情页 UI */
    const val PACKET_OPENED_DETAIL = "LuckyMoneyDetailUI"

  }


  fun currentUI(): String

  fun realScreenWidth(): Int

}