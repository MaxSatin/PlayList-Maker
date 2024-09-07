package com.practicum.playlistmaker.domain.interactors

interface MediaPlayerController {
    fun prepare(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

}