package com.flygo.puremusic.ui.page

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.flygo.architecture.ui.adapter.SimpleBaseBindingAdapter
import com.flygo.puremusic.R
import com.flygo.puremusic.bridge.request.MusicRequestViewModel
import com.flygo.puremusic.bridge.state.HomeViewModel
import com.flygo.puremusic.data.bean.TestAlbum
import com.flygo.puremusic.databinding.AdapterPlayItemBinding
import com.flygo.puremusic.databinding.FragmentMainBinding
import com.flygo.puremusic.player.PlayerManager
import com.flygo.puremusic.ui.base.BaseFragment

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/7
 * 描述：
 * 修订历史：
 */
class MainFragment : BaseFragment() {
    lateinit var homeViewModel: HomeViewModel
    lateinit var musicRequestViewModel: MusicRequestViewModel
    lateinit var mBind:FragmentMainBinding
    lateinit var mAdapter: SimpleBaseBindingAdapter<TestAlbum.TestMusic, AdapterPlayItemBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = getFragmentViewModelProvider().get(HomeViewModel::class.java)
        musicRequestViewModel =
            getFragmentViewModelProvider().get(MusicRequestViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View = inflater.inflate(R.layout.fragment_main,container,false)
        mBind = FragmentMainBinding.bind(view)
        mBind.lifecycleOwner = this
        mBind.vm = homeViewModel
        mBind.click = ClickProxy()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //在BindingAdapter中初始化tabLayout和ViewPager
        homeViewModel.initTabAndPage.set(true)
        homeViewModel.pageAssetPath.set("summary.html")

        mAdapter = object :SimpleBaseBindingAdapter<TestAlbum.TestMusic, AdapterPlayItemBinding>(Utils.getApp().applicationContext,R.layout.adapter_play_item){
            override fun onBindItem(
                binding: AdapterPlayItemBinding,
                item: TestAlbum.TestMusic,
                holder: BaseBindingViewHolder
            ) {
                binding.tvTitle.text = item.title
                binding.tvArtist.text = item.artist.name
                Glide.with(binding.ivCover.context).load(item.coverImg)
                    .into(binding.ivCover)
                val currentIndex: Int = PlayerManager.getAlbumIndex()
//                binding.ivPlayStatus.setColor(
//                    if (currentIndex == holder.adapterPosition) resources.getColor(
//                        R.color.gray
//                    ) else Color.TRANSPARENT
//                )
                binding.root.setOnClickListener {
                    PlayerManager.playAudio(holder.adapterPosition)
                }
            }
        }
        mBind.rv.adapter = mAdapter


        PlayerManager.changeMusicLiveData.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyDataSetChanged()
        })


        musicRequestViewModel.freeMusicsLiveData.observe(viewLifecycleOwner, Observer {
            mAdapter.setData(it.musics)
            if (PlayerManager.album == null || PlayerManager.album?.albumMid != it.albumMid){
                PlayerManager.loadAlbum(it)
            }
        })

        if (PlayerManager.album == null){
            musicRequestViewModel.requestFreeMusics()
        }else{
            with(mAdapter) { setData(PlayerManager.album!!.musics) }
        }
    }

    inner class ClickProxy{
        fun openMenu(){
            mAppViewModel.openOrCloseDrawer.value = true
        }

        fun search(){
            nav().navigate(R.id.action_mainFragment_to_searchFragment)
        }
    }
}