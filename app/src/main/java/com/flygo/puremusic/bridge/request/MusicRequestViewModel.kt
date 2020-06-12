package com.flygo.puremusic.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flygo.puremusic.data.bean.TestAlbum
import com.flygo.puremusic.data.repository.HttpRequestManager

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/7
 * 描述：一个项目中通常存在多个 RequestViewModel
 * 修订历史：
 */
class MusicRequestViewModel : ViewModel() {

    val freeMusicsLiveData : MutableLiveData<TestAlbum> by lazy {
        //最后一行作为返回值
          MutableLiveData<TestAlbum>()
    }

    fun requestFreeMusics() {
        HttpRequestManager.getFreeMusic(freeMusicsLiveData)
    }
}
