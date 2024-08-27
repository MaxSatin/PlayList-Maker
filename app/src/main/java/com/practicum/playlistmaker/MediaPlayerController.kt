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
        private const val STATE_STOP = 4
        private const val TIMER_DELAY = 50L
    }

    private val player = MediaPlayer()
    private val simpleDateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var playerState = STATE_DEFAULT

    private var isRunnableCreated = false

    lateinit var runnable: Runnable
    lateinit var playButton: ToggleButton
    lateinit var handler: Handler

    fun releaseMediaPlayer() {
        player.release()
    }

    fun preparePlayer(url: String, button: ToggleButton, handler: Handler) {
        this.handler = handler
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playButton = button
            button.isChecked = false
            playerState = STATE_PREPARED
        }
    }

    fun startPlayer() {
        player.start()
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        player.pause()
        removeRunnableCallBacks(handler)
        playerState = STATE_PAUSE
        playButton.isChecked = false
    }

    fun playBackControl(textView: TextView) {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSE -> {
                startPlayer()
                showTimeCountDown(textView, handler)
            }

        }
    }

    private fun showTimeCountDown(textView: TextView, handler: Handler) {
        val runnable = object : Runnable {
            override fun run() {
                textView.text =
                    simpleDateFormat.format(player.currentPosition)
                handler.postDelayed(this, TIMER_DELAY)
            }
        }
        this.runnable = runnable
        isRunnableCreated = true

        handler.postDelayed(runnable, TIMER_DELAY)

        player.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler.removeCallbacks(runnable)
            textView.text = simpleDateFormat.format(0)
            playButton.isChecked = false
        }
    }

    fun removeRunnableCallBacks(handler: Handler) {
        if (isRunnableCreated) {
            handler.removeCallbacks(runnable)
        }
    }

}