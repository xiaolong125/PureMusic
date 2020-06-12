package com.flygo.architecture.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
class CommonViewPagerAdapter(val enableDestroyItem:Boolean, val titles:Array<String>) : PagerAdapter() {



    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return container.getChildAt(position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (enableDestroyItem){
            container.removeView(`object` as View?)
        }
    }
}