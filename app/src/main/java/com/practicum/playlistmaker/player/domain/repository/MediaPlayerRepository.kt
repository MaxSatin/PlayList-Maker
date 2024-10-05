package com.practicum.playlistmaker.player.domain.repository

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener

interface MediaPlayerRepository {
    fun preparePlayer(url: String)
    fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener)
    fun playBackControll()
    fun isPlayerPrepared(): Boolean
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun isPlaying(): Boolean
    fun getPlayerCurrentPosition(): Int
    fun setOnCompleteListener(listener: OnCompletionListener)

}