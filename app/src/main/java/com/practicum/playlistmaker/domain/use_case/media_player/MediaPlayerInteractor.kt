package com.practicum.playlistmaker.domain.use_case.media_player

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