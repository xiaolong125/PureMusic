package com.flygo.puremusic.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flygo.puremusic.R
import com.flygo.puremusic.bridge.state.SearchViewModel
import com.flygo.puremusic.databinding.FragmentSearchBinding
import com.flygo.puremusic.ui.base.BaseFragment

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/11
 * 描述：
 * 修订历史：
 */
class SearchFragment : BaseFragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel = getFragmentViewModelProvider().get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        binding = FragmentSearchBinding.bind(view)
        binding.click = ClickProxy()
        binding.vm = searchViewModel
        binding.lifecycleOwner = this
        return view
    }

    inner class ClickProxy{
        fun back(){
            nav().navigateUp()
        }

        fun testNav(){
            val u = "https://xiaozhuanlan.com/topic/5860149732"
            val uri = Uri.parse(u)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        fun subscribe() {
            val u = "https://xiaozhuanlan.com/kunminx"
            val uri = Uri.parse(u)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}