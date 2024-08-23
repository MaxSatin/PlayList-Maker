package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Handler
import android.widget.TextView
import android.widget.ToggleButton
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
    lateinit var runnable: Runnable
    lateinit var handler: Handler

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
        handler.removeCallbacks(runnable)
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
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIMER_DELAY)

            }
        }
        this.runnable = runnable
        this.handler = handler

        handler.postDelayed(runnable, TIMER_DELAY)

        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler.removeCallbacks(runnable)
            textView.text = "00:00"
            playButton.isChecked = false
        }
    }

    fun removeRunnableCallBacks(handler: Handler) {
        handler.removeCallbacks(runnable)
    }
}