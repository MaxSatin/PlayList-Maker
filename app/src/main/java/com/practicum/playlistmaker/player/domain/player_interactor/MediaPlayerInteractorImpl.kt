package com.practicum.playlistmaker.player.domain.player_interactor

import android.media.MediaPlayer

import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository

class MediaPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {

    override fun preparePlayer(url: String) {
        mediaPlayerRepository.preparePlayer(url)
    }

    override fun playBackControll() {
        mediaPlayerRepository.playBackControll()
    }


    override fun playerStart() {
        mediaPlayerRepository.startPlayer()

    }

    override fun isPlaying(): Boolean {
        return mediaPlayerRepository.isPlaying()
    }

    override fun setOnCompleteListener(listener: MediaPlayer.OnCompletionListener) {
        mediaPlayerRepository.setOnCompleteListener(listener)
    }

    override fun playerPause() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        mediaPlayerRepository.releasePlayer()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getPlayerCurrentPosition()
    }


}