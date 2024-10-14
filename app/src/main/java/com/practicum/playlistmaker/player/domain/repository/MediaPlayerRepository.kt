package com.practicum.playlistmaker.player.domain.repository


interface MediaPlayerRepository {
    fun preparePlayer(url: String)
    fun setOnPreparedListener(listener: () -> Unit)
    fun playBackControll()
    fun isPlayerPrepared(): Boolean
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun isPlaying(): Boolean
    fun getPlayerCurrentPosition(): Int
    fun setOnCompleteListener(listener:() -> Unit)

}