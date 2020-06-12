package com.flygo.puremusic.data.repository

import androidx.lifecycle.MutableLiveData
import com.flygo.puremusic.data.bean.LibraryInfo
import com.flygo.puremusic.data.bean.TestAlbum

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
interface IRemoteRequest {

    fun getFreeMusic(liveData:MutableLiveData<TestAlbum>)

    fun getLibraryInfo(liveData: MutableLiveData<List<LibraryInfo>>)
}