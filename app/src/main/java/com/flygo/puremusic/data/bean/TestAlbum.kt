package com.flygo.puremusic.data.bean

import com.kunminx.player.bean.base.BaseAlbumItem
import com.kunminx.player.bean.base.BaseArtistItem
import com.kunminx.player.bean.base.BaseMusicItem

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
data class TestAlbum(val albumMid:String): BaseAlbumItem<TestAlbum.TestMusic,TestAlbum.TestArtist>() {
    inner class TestArtist(val birthday:String) : BaseArtistItem(){
    }
    inner class TestMusic(val songMid:String): BaseMusicItem<TestArtist>(){
    }
}