package com.flygo.puremusic.bridge.state

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.Utils
import com.flygo.puremusic.R
import com.flygo.puremusic.player.PlayerManager
import com.kunminx.player.PlayingInfoManager
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/7
 * 描述：
 * 修订历史：
 */
class PlayerViewModel : ViewModel() {

    val title = ObservableField<String>()
    val artist = ObservableField<String>()
    val coverImg = ObservableField<String>()
    val maxSeekDuration = ObservableField<Int>()
    val currentSeekPosition = ObservableField<Int>()
    val isPlaying = ObservableField<Boolean>()
    val placeHolder = ObservableField<Drawable>()
    val playModeIcon = ObservableField<MaterialDrawableBuilder.IconValue>()

    init {
        title.set(Utils.getApp().getString(R.string.app_name))
        artist.set(Utils.getApp().getString(R.string.app_name))
        placeHolder.set(Utils.getApp().resources.getDrawable(R.drawable.bg_album_default))

        when {
            PlayerManager.repeatMode == PlayingInfoManager.RepeatMode.LIST_LOOP -> {
                playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT)
            }
            PlayerManager.repeatMode == PlayingInfoManager.RepeatMode.ONE_LOOP -> {
                playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE)
            }
            else -> {
                playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE)
            }
        }
    }
}