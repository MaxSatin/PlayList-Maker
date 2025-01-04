package com.practicum.playlistmaker.medialibrary.domain.screen_state

import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track

sealed interface PlaylistDetailsScreenState {

    object Loading: PlaylistDetailsScreenState

    data class Content(
        val contents: List<Track>
    ): PlaylistDetailsScreenState

    data class Empty(
        val message: String
    ): PlaylistDetailsScreenState

}