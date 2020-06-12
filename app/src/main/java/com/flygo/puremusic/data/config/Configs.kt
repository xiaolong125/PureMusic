package com.flygo.puremusic.data.config

import com.blankj.utilcode.util.Utils

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
class Configs {
    companion object{
        var CACHE_PATH:String = Utils.getApp().cacheDir.absolutePath
        var MUSIC_DOWNLOAD_PATH = "$CACHE_PATH/"
    }


}