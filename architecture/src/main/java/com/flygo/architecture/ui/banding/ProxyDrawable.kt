package com.flygo.architecture.ui.banding

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
class ProxyDrawable :StateListDrawable() {
    var originDrawable :Drawable? = null
    override fun addState(stateSet: IntArray?, drawable: Drawable?) {
        if (stateSet?.size ==1 && stateSet[0] == 0){
            originDrawable = drawable
        }
        super.addState(stateSet, drawable)
    }

    fun getDrawable():Drawable?{
        return originDrawable
    }
}