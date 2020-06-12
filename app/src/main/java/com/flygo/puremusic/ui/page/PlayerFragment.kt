package com.flygo.puremusic.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.blankj.utilcode.util.ToastUtils
import com.flygo.puremusic.R
import com.flygo.puremusic.bridge.state.PlayerViewModel
import com.flygo.puremusic.databinding.FragmentPlayerBinding
import com.flygo.puremusic.player.PlayerManager
import com.flygo.puremusic.ui.base.BaseFragment

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/11
 * 描述：
 * 修订历史：
 */
class PlayerFragment :BaseFragment() {

    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var fragmentPlayerBinding: FragmentPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = getFragmentViewModelProvider().get(PlayerViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player,container,false)
        fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        fragmentPlayerBinding.click = ClickProxy()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    inner class ClickProxy:SeekBar.OnSeekBarChangeListener{

        fun playMode() {
            PlayerManager.changeMode()
        }

        fun previous() {
            PlayerManager.playPrevious()
        }

        fun togglePlay() {
            PlayerManager.togglePlay()
        }

        operator fun next() {
            PlayerManager.playNext()
        }

        fun showPlayList() {
            ToastUtils.showShort(R.string.unfinished)
        }

        fun slideDown() {
            mAppViewModel.closeSlidePanelIfExpanded.setValue(true)
        }

        fun more() {}

        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            TODO("Not yet implemented")
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
            TODO("Not yet implemented")
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            TODO("Not yet implemented")
        }

    }
}