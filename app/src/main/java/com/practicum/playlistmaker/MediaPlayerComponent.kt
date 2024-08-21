package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.widget.Button
import android.widget.ToggleButton
import androidx.appcompat.widget.AppCompatImageButton

class MediaPlayerComponent {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSE = 3
    }

    private var playerState = STATE_DEFAULT
    lateinit var playButton: AppCompatImageButton
    lateinit var mediaPlayer: MediaPlayer

    fun createMediaPlayer(){
        mediaPlayer = MediaPlayer()
    }
    fun preparePlayer(url: String, button: AppCompatImageButton) {
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
        when(playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSE -> startPlayer()
        }

    }
}