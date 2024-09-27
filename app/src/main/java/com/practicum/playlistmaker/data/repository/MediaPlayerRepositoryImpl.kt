package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository


class MediaPlayerRepositoryImpl(
    private val player: MediaPlayer
) : MediaPlayerRepository {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSE = 3
    }


    private var playerState = STATE_DEFAULT

    fun getPlayerState(): Int {
        return playerState
    }

    override fun getPlayerCurrentPosition(): Int {
        return player.currentPosition
    }

    override fun setOnCompleteListener(listener: MediaPlayer.OnCompletionListener) {
        player.setOnCompletionListener(listener)
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying
    }

    override fun preparePlayer(url: String) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerState = STATE_PREPARED
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