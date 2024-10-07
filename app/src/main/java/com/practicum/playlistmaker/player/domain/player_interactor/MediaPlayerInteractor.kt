package com.practicum.playlistmaker.player.domain.player_interactor

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener

interface MediaPlayerInteractor {
    fun preparePlayer(url: String)
    fun setOnPreparedListener(listener:() -> Unit)
    fun playBackControll()
    fun isPlayerPrepared(): Boolean
    fun playerStart()
    fun playerPause()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun setOnCompleteListener(listener:() -> Unit)
    fun isPlaying(): Boolean

}