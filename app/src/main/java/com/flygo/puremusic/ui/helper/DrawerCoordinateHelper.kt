/*
 * Copyright 2018-2019 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flygo.puremusic.ui.helper

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.flygo.architecture.bridge.callback.UnPeekLiveData
import com.flygo.puremusic.bridge.state.AppViewModel
import com.flygo.puremusic.ui.base.BaseFragment
import kotlin.math.abs

/**
 * TODO tip：通过 Lifecycle 来解决抽屉侧滑禁用与否的判断的 一致性问题，
 *
 *
 * 每个需要注册和监听生命周期来判断的视图控制器，无需在各自内部手动书写解绑等操作。
 * 如果这样说还不理解，详见 https://xiaozhuanlan.com/topic/3684721950
 *
 *
 * Create by KunMinX at 19/11/3
 */
class DrawerCoordinateHelper private constructor() : DefaultLifecycleObserver, OnTouchListener {
    private var downX = 0f
    private var downY = 0f
    val openDrawer = UnPeekLiveData<Boolean>()
    lateinit var mAppViewModel: AppViewModel

    override fun onCreate(owner: LifecycleOwner) {
        mAppViewModel = (owner as BaseFragment).getAppViewModel()
        mAppViewModel.tagOfSecondaryPages.add(owner.javaClass.simpleName)
        mAppViewModel.enableSwipeDrawer.setValue(mAppViewModel.tagOfSecondaryPages.size == 0)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        mAppViewModel.tagOfSecondaryPages.remove(owner.javaClass.simpleName)
        mAppViewModel.enableSwipeDrawer.setValue(mAppViewModel.tagOfSecondaryPages.size == 0)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = x
                downY = y
            }
            MotionEvent.ACTION_UP -> {
                val dx = x - downX
                val dy = y - downY
                if (abs(dx) > 8 && abs(dy) > 8) {
                    when (getOrientation(dx, dy)) {
                        'r' -> openDrawer.setValue(true)
                        'l' -> {
                        }
                        't' -> {
                        }
                        'b' -> {
                        }
                    }
                }
            }
        }
        return false
    }

    private fun getOrientation(dx: Float, dy: Float): Char {
        return if (abs(dx) > abs(dy)) {
            if (dx > 0) 'r' else 'l'
        } else {
            if (dy > 0) 'b' else 't'
        }
    }
}