package com.practicum.playlistmaker.player.domain.player_interactor


import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository

class MediaPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {

    override fun preparePlayer(url: String) {
        mediaPlayerRepository.preparePlayer(url)
    }

    override fun setOnPreparedListener(listener:() -> Unit){
        mediaPlayerRepository.setOnPreparedListener(listener)
    }

    override fun playBackControll() {
        mediaPlayerRepository.playBackControll()
    }

    override fun isPlayerPrepared(): Boolean {
        return mediaPlayerRepository.isPlayerPrepared()
    }

    override fun playerStart() {
        mediaPlayerRepository.startPlayer()
    }

    override fun seekTo(msec: Int) {
        mediaPlayerRepository.seekTo(msec)
    }

    override fun isPlaying(): Boolean {
        return mediaPlayerRepository.isPlaying()
    }

    override fun setOnCompleteListener(listener:() -> Unit) {
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

    override fun resetPlayer() {
        mediaPlayerRepository.resetPlayer()
    }

}