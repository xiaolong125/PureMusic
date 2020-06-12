package com.flygo.puremusic.player

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.flygo.puremusic.data.bean.TestAlbum
import com.flygo.puremusic.data.bean.TestAlbum.*
import com.kunminx.player.PlayerController
import com.kunminx.player.bean.base.BaseAlbumItem
import com.kunminx.player.bean.base.BaseArtistItem
import com.kunminx.player.bean.base.BaseMusicItem
import com.kunminx.player.bean.dto.ChangeMusic
import com.kunminx.player.bean.dto.PlayingMusic
import com.kunminx.player.contract.IPlayController
import com.kunminx.player.contract.IServiceNotifier

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
//object关键字创建的单例不能带参数
 object PlayerManager : IPlayController<TestAlbum, TestMusic> {

    private var mPlayerController:PlayerController<TestAlbum, TestMusic> = PlayerController()

    //    companion object : SingletonHolder<PlayerManager, PlayerController<TestAlbum, TestAlbum.TestMusic>>(::PlayerManager)
    private lateinit var mContext:Context

    override fun clear() {
        mPlayerController.clear(mContext)
    }

    override fun requestLastPlayingInfo() {
        mPlayerController.requestLastPlayingInfo()
    }

    override fun getPlayModeLiveData(): MutableLiveData<Enum<*>> {
        return mPlayerController.playModeLiveData
    }

    override fun loadAlbum(musicAlbum: TestAlbum?) {
        mPlayerController.loadAlbum(mContext,musicAlbum)
    }

    override fun loadAlbum(musicAlbum: TestAlbum?, playIndex: Int) {
        mPlayerController.loadAlbum(mContext,musicAlbum,playIndex)
    }

    fun init(context: Context) {
        init(context, null)
    }

    override fun init(context: Context, iServiceNotifier: IServiceNotifier?) {
        mContext = context.applicationContext

    }

    override fun setChangingPlayingMusic(changingPlayingMusic: Boolean) {
        mPlayerController.setChangingPlayingMusic(mContext,changingPlayingMusic)
    }

    override fun changeMode() {
        mPlayerController.changeMode()
    }

    override fun playAudio() {
        mPlayerController.playAudio(mContext)
    }

    override fun playAudio(albumIndex: Int) {
        mPlayerController.playAudio(mContext,albumIndex)
    }

    override fun getPlayingMusicLiveData(): MutableLiveData<PlayingMusic<BaseArtistItem, BaseAlbumItem<*, *>>> {
        return mPlayerController.playingMusicLiveData
    }

    override fun getPauseLiveData(): MutableLiveData<Boolean> {
        return mPlayerController.pauseLiveData
    }

    override fun getAlbum(): TestAlbum? {
        return mPlayerController.album
    }

    override fun playAgain() {
        mPlayerController.playAgain(mContext)
    }

    override fun isPaused(): Boolean {
        return mPlayerController.isPaused
    }

    override fun isPlaying(): Boolean {
        return mPlayerController.isPlaying
    }

    override fun getRepeatMode(): Enum<out Enum<*>>? {
        return mPlayerController.repeatMode
    }

    override fun getChangeMusicLiveData(): MutableLiveData<ChangeMusic<BaseAlbumItem<*, *>, BaseMusicItem<*>, BaseArtistItem>> {
        return mPlayerController.changeMusicLiveData
    }

    override fun setSeek(progress: Int) {
        mPlayerController.setSeek(progress)
    }

    override fun getAlbumIndex(): Int {
        return mPlayerController.albumIndex
    }

    override fun playPrevious() {
        mPlayerController.playPrevious(mContext)
    }

    override fun resumeAudio() {
        mPlayerController.resumeAudio()
    }

    override fun pauseAudio() {
        mPlayerController.pauseAudio()
    }

    override fun isInited(): Boolean {
        return mPlayerController.isInited
    }

    override fun getAlbumMusics(): MutableList<TestMusic>? {
        return mPlayerController.albumMusics
    }

    override fun getCurrentPlayingMusic(): TestMusic {
        return mPlayerController.currentPlayingMusic
    }

    override fun togglePlay() {
        mPlayerController.togglePlay(mContext)
    }

    override fun playNext() {
        mPlayerController.playNext(mContext)
    }

    override fun getTrackTime(progress: Int): String {
        return mPlayerController.getTrackTime(progress)
    }
}