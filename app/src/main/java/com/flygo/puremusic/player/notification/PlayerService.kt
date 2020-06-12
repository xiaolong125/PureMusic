package com.flygo.puremusic.player.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.ImageUtils
import com.flygo.puremusic.ui.MainActivity
import com.flygo.puremusic.R
import com.flygo.puremusic.data.bean.TestAlbum
import com.flygo.puremusic.data.config.Configs
import com.flygo.puremusic.player.PlayerManager
import com.flygo.puremusic.player.helper.PlayerCallHelper
import com.flygo.puremusic.player.helper.PlayerCallHelperListener
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.model.Response

import java.io.File

/**
 * Create by KunMinX at 19/7/17
 */
class PlayerService : Service() {
    private var mPlayerCallHelper: PlayerCallHelper? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (mPlayerCallHelper == null) {
            mPlayerCallHelper = PlayerCallHelper(object : PlayerCallHelperListener {

                override fun playAudio() {
                    PlayerManager.playAudio()
                }

                override fun pauseAudio() {
                    PlayerManager.pauseAudio()
                }

                override fun isPlaying(): Boolean {
                    return PlayerManager.isPlaying
                }

                override fun isPaused(): Boolean {
                    return PlayerManager.isPaused
                }
            })
        }
        val results: TestAlbum.TestMusic = PlayerManager.getCurrentPlayingMusic()
        if (results == null) {
            stopSelf()
            return START_NOT_STICKY
        }
        mPlayerCallHelper?.bindCallListener(applicationContext)
        createNotification(results)
        return START_NOT_STICKY
    }

    private fun createNotification(testMusic: TestAlbum.TestMusic) {
        try {
            val title: String = testMusic.getTitle()
            val album: TestAlbum? = PlayerManager.getAlbum()
            val summary: String? = album?.getSummary()
            val simpleContentView = RemoteViews(
                applicationContext.packageName, R.layout.notify_player_small
            )
            var expandedView: RemoteViews? = null
            expandedView = RemoteViews(
                applicationContext.packageName, R.layout.notify_player_big
            )
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.action = "showPlayer"
            val contentIntent = PendingIntent.getActivity(
                this, 0, intent, 0
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val playGroup = NotificationChannelGroup(
                    GROUP_ID,
                    getString(R.string.play)
                )
                notificationManager.createNotificationChannelGroup(playGroup)
                val playChannel = NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.notify_of_play), NotificationManager.IMPORTANCE_DEFAULT
                )
                playChannel.group = GROUP_ID
                notificationManager.createNotificationChannel(playChannel)
            }
            val notification = NotificationCompat.Builder(
                applicationContext, CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_player)
                .setContentIntent(contentIntent)
                .setOnlyAlertOnce(true)
                .setContentTitle(title).build()
            notification.contentView = simpleContentView
            notification.bigContentView = expandedView
            setListeners(simpleContentView)
            setListeners(expandedView)
            notification.contentView.setViewVisibility(
                R.id.player_progress_bar,
                View.GONE
            )
            notification.contentView.setViewVisibility(R.id.player_next, View.VISIBLE)
            notification.contentView.setViewVisibility(
                R.id.player_previous,
                View.VISIBLE
            )
            notification.bigContentView.setViewVisibility(
                R.id.player_next,
                View.VISIBLE
            )
            notification.bigContentView.setViewVisibility(
                R.id.player_previous,
                View.VISIBLE
            )
            notification.bigContentView.setViewVisibility(
                R.id.player_progress_bar,
                View.GONE
            )
            val isPaused: Boolean = PlayerManager.isPaused()
            notification.contentView.setViewVisibility(
                R.id.player_pause,
                if (isPaused) View.GONE else View.VISIBLE
            )
            notification.contentView.setViewVisibility(
                R.id.player_play,
                if (isPaused) View.VISIBLE else View.GONE
            )
            notification.bigContentView.setViewVisibility(
                R.id.player_pause,
                if (isPaused) View.GONE else View.VISIBLE
            )
            notification.bigContentView.setViewVisibility(
                R.id.player_play,
                if (isPaused) View.VISIBLE else View.GONE
            )
            notification.contentView.setTextViewText(R.id.player_song_name, title)
            notification.contentView.setTextViewText(R.id.player_author_name, summary)
            notification.bigContentView.setTextViewText(R.id.player_song_name, title)
            notification.bigContentView.setTextViewText(R.id.player_author_name, summary)
            notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
            val coverPath: String =
                Configs.MUSIC_DOWNLOAD_PATH + testMusic.getMusicId().toString() + ".jpg"
            val bitmap: Bitmap = ImageUtils.getBitmap(coverPath)
            if (bitmap != null) {
                notification.contentView.setImageViewBitmap(R.id.player_album_art, bitmap)
                notification.bigContentView.setImageViewBitmap(R.id.player_album_art, bitmap)
            } else {
                requestAlbumCover(testMusic.coverImg, testMusic.getMusicId())
                notification.contentView.setImageViewResource(
                    R.id.player_album_art,
                    R.drawable.bg_album_default
                )
                notification.bigContentView.setImageViewResource(
                    R.id.player_album_art,
                    R.drawable.bg_album_default
                )
            }
            startForeground(5, notification)
            mPlayerCallHelper?.bindRemoteController(applicationContext)
            mPlayerCallHelper?.requestAudioFocus(title, summary)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setListeners(view: RemoteViews) {
        try {
            var pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0, Intent(NOTIFY_PREVIOUS).setPackage(packageName),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            view.setOnClickPendingIntent(R.id.player_previous, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0, Intent(NOTIFY_CLOSE).setPackage(packageName),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            view.setOnClickPendingIntent(R.id.player_close, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0, Intent(NOTIFY_PAUSE).setPackage(packageName),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            view.setOnClickPendingIntent(R.id.player_pause, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0, Intent(NOTIFY_NEXT).setPackage(packageName),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            view.setOnClickPendingIntent(R.id.player_next, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0, Intent(NOTIFY_PLAY).setPackage(packageName),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            view.setOnClickPendingIntent(R.id.player_play, pendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestAlbumCover(coverUrl: String, musicId: String) {
        OkGo.get<File>(coverUrl)
            .execute(object : FileCallback(Configs.MUSIC_DOWNLOAD_PATH, "$musicId.jpg") {
                override fun onSuccess(response: Response<File>) {
                    startService(Intent(applicationContext, PlayerService::class.java))
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayerCallHelper?.let {
            it.unbindCallListener(applicationContext)
            it.unbindRemoteController()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private const val GROUP_ID = "group_001"
        private const val CHANNEL_ID = "channel_001"
        const val NOTIFY_PREVIOUS = "pure_music.kunminx.previous"
        const val NOTIFY_CLOSE = "pure_music.kunminx.close"
        const val NOTIFY_PAUSE = "pure_music.kunminx.pause"
        const val NOTIFY_PLAY = "pure_music.kunminx.play"
        const val NOTIFY_NEXT = "pure_music.kunminx.next"
    }
}