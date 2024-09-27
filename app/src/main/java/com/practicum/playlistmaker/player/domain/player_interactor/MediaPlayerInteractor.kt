package com.practicum.playlistmaker.player.domain.player_interactor

import android.media.MediaPlayer.OnCompletionListener

interface MediaPlayerInteractor {
    fun preparePlayer(url: String)
    fun playerStart()
    fun playerPause()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun setOnCompleteListener(listener: OnCompletionListener)
    fun isPlaying(): Boolean

}