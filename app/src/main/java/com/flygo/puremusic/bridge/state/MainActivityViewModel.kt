package com.flygo.puremusic.bridge.state

import androidx.lifecycle.ViewModel
import com.flygo.architecture.bridge.callback.UnPeekLiveData

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：MainActivity 的viewModel
 * 修订历史：
 */
class MainActivityViewModel : ViewModel() {
    val openDrawer = UnPeekLiveData<Boolean>()
    val allowDrawerOpen = UnPeekLiveData<Boolean>()
    val enableSwipeDrawer = UnPeekLiveData<Boolean>()

    init {
        allowDrawerOpen.value = true
    }
}