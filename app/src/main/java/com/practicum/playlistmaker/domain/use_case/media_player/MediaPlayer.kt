package com.practicum.playlistmaker.domain.use_case.media_player

import android.media.MediaPlayer
import android.os.Handler
import android.widget.TextView
import com.practicum.playlistmaker.MediaPlayerController.Companion
import com.practicum.playlistmaker.domain.interactors.MediaPlayerController
import retrofit2.Callback

class MediaPlayer() : MediaPlayerController {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSE = 3
        private const val STATE_STOP = 4
        private const val TIMER_DELAY = 50L
    }

    private val player: MediaPlayer by lazy { MediaPlayer() }
    private var playerState = STATE_DEFAULT

    override fun prepare(url: String) {
        player.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
            }
        }
    }

    override fun startPlayer() {
        player.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        player.pause()
        playerState = STATE_PAUSE
    }

    override fun releasePlayer() {
        player.release()
    }
}