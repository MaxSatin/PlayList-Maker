package com.practicum.playlistmaker.player.presentation.state

import com.practicum.playlistmaker.player.presentation.model.Track

sealed interface PlayerState {
    object Loading : PlayerState
    data class Content(val track: Track): PlayerState
}