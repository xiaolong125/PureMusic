package com.flygo.puremusic.bridge.state

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/7
 * 描述：
 * 修订历史：
 */
class HomeViewModel : ViewModel() {
    val initTabAndPage = ObservableBoolean()
    val pageAssetPath = ObservableField<String>()
}