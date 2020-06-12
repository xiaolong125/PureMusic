package com.flygo.puremusic.player.helper

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
interface PlayerCallHelperListener {

    fun playAudio()

    fun pauseAudio()

    fun isPlaying():Boolean

    fun isPaused():Boolean
}