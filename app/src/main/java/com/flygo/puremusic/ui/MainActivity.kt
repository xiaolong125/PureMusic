package com.flygo.puremusic.ui

import android.os.Bundle
import android.view.Gravity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.flygo.puremusic.R
import com.flygo.puremusic.bridge.state.MainActivityViewModel
import com.flygo.puremusic.databinding.ActivityMainBinding
import com.flygo.puremusic.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    var isListened = false

    lateinit var mMainActivityViewModel : MainActivityViewModel
    lateinit var binding :ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainActivityViewModel = getActivityViewModelProvide().get(MainActivityViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = mMainActivityViewModel
        //加了下面的代码绑定LiveData数据源的xml才会随着数据变化而变化
        binding.lifecycleOwner = this
        val nav = Navigation.findNavController(this, R.id.main_fragment_host)
        //监听主界面的抽屉布局，返回上一个fragment或者关闭activity
        mAppViewModel.activityCanBeClosedDirectly.observe(this, observer = Observer {
                if (it){
                    when {
                        nav.currentDestination?.id != R.id.mainFragment -> {
                            //返回上一级，这里指返回mainFragment
                            nav.navigateUp()
                        }
                        binding.dl!!.isDrawerOpen(GravityCompat.START) -> {
                            binding.dl?.closeDrawer(GravityCompat.START)
                        }
                        else -> {
                            super.onBackPressed()
                        }
                    }
                }
            })

        mAppViewModel.openOrCloseDrawer.observe(this, Observer {
            mMainActivityViewModel.openDrawer.value = it
        })

        mAppViewModel.enableSwipeDrawer.observe(this, Observer {
            mMainActivityViewModel.enableSwipeDrawer.value = it
        })

        //监听主界面抽屉布局
        mMainActivityViewModel.openDrawer.observe(this, Observer {
            if (it){
                //注意横屏没有抽屉
                binding.dl?.openDrawer(GravityCompat.START)
            }else{
                binding.dl?.closeDrawer(GravityCompat.START)
            }
        })

        mMainActivityViewModel.enableSwipeDrawer.observe(this, Observer {
            binding.dl?.setDrawerLockMode(if (it) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!isListened){
            mAppViewModel.timeToAddSlideListener.value = true
            isListened = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mAppViewModel.closeSlidePanelIfExpanded.value = true
    }
}