package com.flygo.puremusic

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/5
 * 描述：
 * 修订历史：
 */
class App:Application(), ViewModelStoreOwner{

    //tip：可借助 Application 来管理一个应用级 的 SharedViewModel，
    // 实现全应用范围内的 生命周期安全 且 事件源可追溯的 视图控制器 事件通知。
    private lateinit var mAppViewModelStore:ViewModelStore
    override fun onCreate() {
        super.onCreate()
    }

    override fun getViewModelStore(): ViewModelStore {
         mAppViewModelStore = ViewModelStore()
        return mAppViewModelStore
    }
}