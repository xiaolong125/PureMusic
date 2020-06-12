package com.flygo.puremusic.player.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import com.flygo.puremusic.player.PlayerManager

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
class PlayerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }
        if (TextUtils.equals(Intent.ACTION_MEDIA_BUTTON, intent.action)) {
            if (intent.extras == null) {
                return
            }
//            val keyEvent = intent?.extras.get(Intent.EXTRA_KEY_EVENT) as KeyEvent

            val keyEvent =
                intent.extras?.get(Intent.EXTRA_KEY_EVENT) as KeyEvent? ?: return

            if (keyEvent.action != KeyEvent.ACTION_DOWN) {
                return
            }

            when (keyEvent.keyCode) {
                KeyEvent.KEYCODE_HEADSETHOOK, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> PlayerManager.togglePlay()
                KeyEvent.KEYCODE_MEDIA_PLAY -> PlayerManager.playAudio()
                KeyEvent.KEYCODE_MEDIA_PAUSE -> PlayerManager.pauseAudio()
                KeyEvent.KEYCODE_MEDIA_STOP -> PlayerManager.clear()
                KeyEvent.KEYCODE_MEDIA_NEXT -> PlayerManager.playNext()
                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> PlayerManager.playPrevious()
            }
        }
    }
}