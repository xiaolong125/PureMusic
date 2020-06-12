package com.flygo.puremusic.bridge.state

import androidx.lifecycle.ViewModel
import com.flygo.architecture.bridge.callback.UnPeekLiveData
import java.util.*

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/7
 * 描述：全局ViewModel
 * 修订历史：
 */
class AppViewModel :ViewModel(){

    // TODO tip 1：此处演示通过 UnPeekLiveData 配合 SharedViewModel 来发送 生命周期安全的、事件源可追溯的 通知。
    // 并且，使用 Application 级的 SharedViewModel，而不是单例，来负责 全局的页面消息通信，是为了 约束作用域，
    // 以免视图控制器间的消息 污染到 视图控制器之外的领域。
    // 如果这么说还不理解的话，
    // 详见 https://xiaozhuanlan.com/topic/0168753249 和 https://xiaozhuanlan.com/topic/6257931840
    val timeToAddSlideListener = UnPeekLiveData<Boolean>()

    val closeSlidePanelIfExpanded = UnPeekLiveData<Boolean>()

    val activityCanBeClosedDirectly = UnPeekLiveData<Boolean>()

    val openOrCloseDrawer = UnPeekLiveData<Boolean>()

    val enableSwipeDrawer = UnPeekLiveData<Boolean>()

    val tagOfSecondaryPages: MutableList<String> = ArrayList()

}