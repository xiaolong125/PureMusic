package com.flygo.puremusic.ui.base

import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.flygo.puremusic.App
import com.flygo.puremusic.bridge.state.AppViewModel

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/7
 * 描述：
 * 修订历史：
 */
open class BaseActivity : AppCompatActivity() {
    protected lateinit var mAppViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this, true)
        mAppViewModel = getAppViewModelProvider().get(AppViewModel::class.java)
    }

    @SuppressWarnings("unused")
    protected fun isDebug(): Boolean {
        return applicationContext.applicationInfo != null &&
                applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    override fun getResources(): Resources {
        return if (ScreenUtils.isPortrait()) {
            AdaptScreenUtils.adaptWidth(super.getResources(), 360)
        } else {
            AdaptScreenUtils.adaptHeight(super.getResources(), 640)
        }
    }

    private fun getAppViewModelProvider(): ViewModelProvider {
        return (applicationContext as App).getAppViewModelProvider(this)
    }

    protected fun getActivityViewModelProvide():ViewModelProvider{
        return ViewModelProvider(this,defaultViewModelProviderFactory)
    }
}