package com.practicum.playlistmaker.player.domain.repository

import android.media.MediaPlayer.OnCompletionListener

interface MediaPlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun isPlaying(): Boolean
    fun getPlayerCurrentPosition(): Int
    fun setOnCompleteListener(listener: OnCompletionListener)

}