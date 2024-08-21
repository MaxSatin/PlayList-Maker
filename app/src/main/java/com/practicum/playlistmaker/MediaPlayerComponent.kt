package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.widget.Button
import android.widget.ToggleButton

class MediaPlayerComponent {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSE = 3
    }

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    lateinit var playButton: Button


    private fun preparePlayer(url: String, button: Button) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton = button
            button.isEnabled = true
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSE
    }

    private fun playBackControl() {
        when(playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSE -> startPlayer()
        }

    }
}