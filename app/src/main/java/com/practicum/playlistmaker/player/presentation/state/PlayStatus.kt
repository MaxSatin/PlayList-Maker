package com.practicum.playlistmaker.player.presentation.state

data class PlayStatus(
    var isPrepared: Boolean,
    val progress: String,
    val isPlaying: Boolean
)
