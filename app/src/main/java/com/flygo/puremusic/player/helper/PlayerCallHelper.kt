package com.flygo.puremusic.player.helper

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaMetadataRetriever
import android.media.RemoteControlClient
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import com.flygo.puremusic.player.notification.PlayerReceiver

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：在来电时自动协调和暂停音乐播放
 * 修订历史：
 */
//构造方法加上了val或者var之后才能在类中使用这个属性，没加的话相当于在构造方法内部创建了一个变量
class PlayerCallHelper(val mPlayerCallHelperListener: PlayerCallHelperListener): OnAudioFocusChangeListener {
    private var phoneStateListener: PhoneStateListener? = null
    private var mIsTempPauseByPhone:Boolean = false
    private var mAudioManager: AudioManager? = null
    private var remoteControlClient :RemoteControlClient? = null
    private var ignoreAudioFocus:Boolean = false
    private var tempPause:Boolean = false

    fun bindCallListener(context: Context){
        //创建匿名类使用object关键字
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                when (state) {
                    TelephonyManager.CALL_STATE_IDLE -> {
                        mPlayerCallHelperListener.playAudio()
                        mIsTempPauseByPhone = false
                    }
                    TelephonyManager.CALL_STATE_RINGING -> {
                        if (mPlayerCallHelperListener.isPlaying() &&
                            !mPlayerCallHelperListener.isPaused()) {
                            mPlayerCallHelperListener.pauseAudio()
                            mIsTempPauseByPhone = true
                        }
                    }
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                    }
                }
                super.onCallStateChanged(state, incomingNumber)
            }
        }

        val manager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    fun bindRemoteController(context: Context) {
        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val remoteComponentName = ComponentName(context, PlayerReceiver::class.java.getName())
        try {
            if (remoteControlClient == null) {
                mAudioManager?.registerMediaButtonEventReceiver(remoteComponentName)
                val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                mediaButtonIntent.component = remoteComponentName
                val mediaPendingIntent = PendingIntent.getBroadcast(
                    context, 0, mediaButtonIntent, 0
                )
                remoteControlClient = RemoteControlClient(mediaPendingIntent)
                mAudioManager?.registerRemoteControlClient(remoteControlClient)
            }
            remoteControlClient!!.setTransportControlFlags(
                RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                        or RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
                        or RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE
                        or RemoteControlClient.FLAG_KEY_MEDIA_STOP
                        or RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
                        or RemoteControlClient.FLAG_KEY_MEDIA_NEXT
            )

        } catch (e: Exception) {
            Log.e("tmessages", e.toString())
        }
    }

    fun unbindCallListener(context: Context) {
        try {
            val mgr =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            mgr?.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        } catch (e: java.lang.Exception) {
            Log.e("tmessages", e.toString())
        }
    }

    fun unbindRemoteController() {
        remoteControlClient?.let {
            val metadataEditor =
                it?.editMetadata(true)
            metadataEditor.clear()
            metadataEditor.apply()
            mAudioManager?.unregisterRemoteControlClient(it)
            mAudioManager?.abandonAudioFocus(this)
        }
    }

    fun requestAudioFocus(title: String?, summary: String?) {
        if (remoteControlClient != null) {
            val metadataEditor =
                remoteControlClient!!.editMetadata(true)
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, summary)
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, title)
            metadataEditor.apply()
            mAudioManager!!.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    fun setIgnoreAudioFocus() {
        ignoreAudioFocus = true
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (ignoreAudioFocus) {
            ignoreAudioFocus = false
            return
        }
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            if (mPlayerCallHelperListener != null) {
                if (mPlayerCallHelperListener.isPlaying() &&
                    !mPlayerCallHelperListener.isPaused()
                ) {
                    mPlayerCallHelperListener.pauseAudio()
                    tempPause = true
                }
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            if (tempPause) {
                mPlayerCallHelperListener?.playAudio()
                tempPause = false
            }
        }
    }
}