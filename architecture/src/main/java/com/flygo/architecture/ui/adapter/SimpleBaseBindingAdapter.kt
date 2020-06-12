package com.flygo.architecture.ui.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
abstract class SimpleBaseBindingAdapter<M,B:ViewDataBinding>(context: Context, private val layout:Int) :BaseBindingAdapter<M,B>(context) {
    override fun getLayoutResId(viewType: Int): Int {
        return layout
    }
}