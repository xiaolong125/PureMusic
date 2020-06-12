package com.flygo.puremusic.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flygo.puremusic.R
import com.flygo.puremusic.bridge.state.DrawerViewModel
import com.flygo.puremusic.databinding.FragmentDrawerBinding
import com.flygo.puremusic.ui.base.BaseFragment

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/11
 * 描述：
 * 修订历史：
 */
class DrawerFragment : BaseFragment() {

    private lateinit var drawerViewModel: DrawerViewModel
    private lateinit var binding: FragmentDrawerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerViewModel = getFragmentViewModelProvider().get(DrawerViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_drawer, container, false)
        binding = FragmentDrawerBinding.bind(view)
        binding.vm =drawerViewModel
        binding.click = ClickProxy()
        return view
    }

    inner class ClickProxy{
        fun logoClick() {
            val u = "https://github.com/KunMinX/Jetpack-MVVM-Best-Practice"
            val uri = Uri.parse(u)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}