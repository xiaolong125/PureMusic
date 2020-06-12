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
package com.flygo.puremusic.ui.adapter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.flygo.architecture.ui.adapter.CommonViewPagerAdapter
import com.flygo.puremusic.R
import com.flygo.puremusic.ui.view.PlayPauseView
import com.google.android.material.tabs.TabLayout
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue
import net.steamcrafted.materialiconlib.MaterialIconView

/**
 * Create by KunMinX at 19/9/18
 */
object AdapterBinding {
    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "placeHolder"], requireAll = false)
    fun loadUrl(
        view: ImageView,
        url: String?,
        placeHolder: Drawable?
    ) {
        Glide.with(view.context).load(url).placeholder(placeHolder).into(view)
    }

    @JvmStatic
    @BindingAdapter(value = ["visible"], requireAll = false)
    fun visible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter(value = ["showDrawable", "drawableShowed"], requireAll = false)
    fun showDrawable(
        view: ImageView,
        showDrawable: Boolean,
        drawableShowed: Int
    ) {
        view.setImageResource(if (showDrawable) drawableShowed else R.color.transparent)
    }

    @JvmStatic
    @BindingAdapter(value = ["textColor"], requireAll = false)
    fun setTextColor(textView: TextView, textColorRes: Int) {
        textView.setTextColor(textView.resources.getColor(textColorRes))
    }

    @JvmStatic
    @BindingAdapter(value = ["imageRes"], requireAll = false)
    fun setImageRes(imageView: ImageView, imageRes: Int) {
        imageView.setImageResource(imageRes)
    }

    @JvmStatic
    @BindingAdapter(value = ["selected"], requireAll = false)
    fun selected(view: View, select: Boolean) {
        view.isSelected = select
    }

    @JvmStatic
    @BindingAdapter(value = ["isOpenDrawer"], requireAll = false)
    fun openDrawer(drawerLayout: DrawerLayout, isOpenDrawer: Boolean) {
        if (isOpenDrawer && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        } else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["isPlaying"], requireAll = false)
    fun isPlaying(pauseView: PlayPauseView, isPlaying: Boolean) {
        if (isPlaying) {
            pauseView.play()
        } else {
            pauseView.pause()
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["initTabAndPage"], requireAll = false)
    fun initTabAndPage(tabLayout: TabLayout, initTabAndPage: Boolean) {
        if (initTabAndPage) {
            val count = tabLayout.tabCount
            val title : Array<String> =  Array<String>(count) {
                //指定每个元素，it指代位置
                tabLayout.getTabAt(it)?.text.toString()
            }
            val viewPager: ViewPager = tabLayout.rootView.findViewById(R.id.view_pager)
            viewPager.adapter = CommonViewPagerAdapter(false, title)
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["mdIcon"], requireAll = false)
    fun setIcon(view: MaterialIconView, iconValue: IconValue?) {
        view.setIcon(iconValue)
    }

    @JvmStatic
    @BindingAdapter(value = ["pageAssetPath"], requireAll = false)
    fun loadAssetsPage(webView: WebView, assetPath: String) {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val uri = request.url
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                Utils.getApp().startActivity(intent)
                return true
            }
        }
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        val url = "file:///android_asset/$assetPath"
        webView.loadUrl(url)
    }

    @JvmStatic
    @BindingAdapter(value = ["loadPage"], requireAll = false)
    fun loadPage(webView: WebView, loadPage: String?) {
        webView.webViewClient = WebViewClient()
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webView.loadUrl(loadPage)
    }

    @JvmStatic
    @BindingAdapter(value = ["allowDrawerOpen"], requireAll = false)
    fun allowDrawerOpen(drawerLayout: DrawerLayout, allowDrawerOpen: Boolean) {
        drawerLayout.setDrawerLockMode(if (allowDrawerOpen) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
}