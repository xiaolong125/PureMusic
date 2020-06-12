package com.flygo.puremusic.data.repository

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.Utils
import com.flygo.puremusic.R
import com.flygo.puremusic.data.bean.LibraryInfo
import com.flygo.puremusic.data.bean.TestAlbum
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
object HttpRequestManager: IRemoteRequest,ILocalRequest {

    override fun getFreeMusic(liveData: MutableLiveData<TestAlbum>) {
        val gson = Gson()
        //创建了一个继承自TypeToken的匿名内部类，再调用type方法
        val type = object : TypeToken<TestAlbum?>() {}.type
        val testAlbum =
            gson.fromJson<TestAlbum>(Utils.getApp().getString(R.string.free_music_json), type)
        liveData.value = testAlbum
    }

    override fun getLibraryInfo(liveData: MutableLiveData<List<LibraryInfo>>) {
        val gson = Gson()
        val type: Type = object : TypeToken<List<LibraryInfo>>(){}.type
        val list: List<LibraryInfo> =
            gson.fromJson(Utils.getApp().getString(R.string.library_json), type)
        liveData.value = list
    }
}