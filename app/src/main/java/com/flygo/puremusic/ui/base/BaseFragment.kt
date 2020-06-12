package com.flygo.puremusic.ui.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.flygo.puremusic.App
import com.flygo.puremusic.bridge.state.AppViewModel

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/7
 * 描述：
 * 修订历史：
 */
open class BaseFragment:Fragment() {
    protected lateinit var mAppViewModel: AppViewModel
    protected lateinit var mActivity:AppCompatActivity
    private val sHandler = Handler()
    protected var mAnimationLoaded = false
    protected var mInitDataCame = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAppViewModel = getAppViewModelProvider().get(AppViewModel::class.java)
    }

    fun getAppViewModelProvider(): ViewModelProvider {
        return (mActivity.applicationContext as App).getAppViewModelProvider(mActivity)
    }

    protected fun getFragmentViewModelProvider():ViewModelProvider{
        return ViewModelProvider(this,defaultViewModelProviderFactory)
    }

    protected fun nav():NavController{
        return NavHostFragment.findNavController(this)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        sHandler.postDelayed({
            mAnimationLoaded = true
            if (mInitDataCame){
                loadInitData()
            }
        },280)
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    protected open fun loadInitData() {
    }

    fun getAppViewModel():AppViewModel{
        return mAppViewModel
    }
}