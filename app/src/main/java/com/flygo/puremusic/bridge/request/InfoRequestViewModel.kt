package com.flygo.puremusic.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flygo.puremusic.data.bean.LibraryInfo
import com.flygo.puremusic.data.repository.HttpRequestManager

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/7
 * 描述：
 * 修订历史：
 */
class InfoRequestViewModel:ViewModel() {
    private val libraryLiveData : MutableLiveData<List<LibraryInfo>> by lazy {
        MutableLiveData<List<LibraryInfo>>()
    }

    fun requestLibraryInfo(){
        HttpRequestManager.getLibraryInfo(libraryLiveData)
    }
}