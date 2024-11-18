package com.practicum.playlistmaker.player.domain.player_interactor


interface MediaPlayerInteractor {
    fun preparePlayer(url: String)
    fun setOnPreparedListener(listener:() -> Unit)
    fun playBackControll()
    fun isPlayerPrepared(): Boolean
    fun playerStart()
    fun seekTo(msec: Int)
    fun playerPause()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun setOnCompleteListener(listener:() -> Unit)
    fun isPlaying(): Boolean

}