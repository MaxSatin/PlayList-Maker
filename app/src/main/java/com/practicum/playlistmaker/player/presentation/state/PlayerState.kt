package com.practicum.playlistmaker.player.presentation.state

import com.practicum.playlistmaker.player.presentation.model.Track
import com.practicum.playlistmaker.player.presentation.model.TrackInfoModel

sealed interface PlayerState {
    object Loading : PlayerState
    data class Content(val track: TrackInfoModel): PlayerState
}