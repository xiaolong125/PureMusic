package com.flygo.puremusic

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.blankj.utilcode.util.Utils
import com.flygo.puremusic.player.PlayerManager

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
    private var mFactory:ViewModelProvider.Factory? = null
    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()

        Utils.init(this)

        PlayerManager.init(this)

    }

    override fun getViewModelStore(): ViewModelStore {
         mAppViewModelStore = ViewModelStore()
        return mAppViewModelStore
    }

    fun getAppViewModelProvider(activity: Activity):ViewModelProvider {
        return ViewModelProvider(
            (activity.applicationContext as App),
            (activity.applicationContext as App).getAppFactory(activity)
        )
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory {
        val application = checkApplication(activity)
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory as ViewModelProvider.Factory
    }

    private fun checkApplication(activity: Activity):Application{
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }
}