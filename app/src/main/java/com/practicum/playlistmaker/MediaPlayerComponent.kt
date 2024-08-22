package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.widget.AppCompatImageButton
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerController {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSE = 3
        private const val TIMER_DELAY = 50L
    }

    private var playerState = STATE_DEFAULT
    lateinit var playButton: ToggleButton
    lateinit var mediaPlayer: MediaPlayer

    fun releaseMediaPlayer() {
        mediaPlayer.release()
    }

    fun createMediaPlayer() {
        mediaPlayer = MediaPlayer()
    }

    fun preparePlayer(url: String, button: ToggleButton) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton = button
            button.isEnabled = true
            playerState = STATE_PREPARED
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSE
    }

    fun playBackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSE -> startPlayer()
        }

    }

    fun playTimeCountDown(textView: TextView, handler: Handler) {
        val runnable = object : Runnable {
            override fun run() {
                textView.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIMER_DELAY)
                mediaPlayer.setOnCompletionListener {
                    playerState = STATE_PREPARED
                    handler.removeCallbacks(this)
                    textView.text = "00:00"
                }
            }
        }
        when (playerState) {
            STATE_PLAYING -> {
                handler.postDelayed(runnable, TIMER_DELAY)
            }
            STATE_PAUSE -> {
                handler.removeCallbacks(runnable)
            }
        }
    }
}