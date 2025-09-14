package com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist

sealed interface PlaylistState {

    data class DetailsState(
        val playlist: Playlist?,
    ): PlaylistState

    data class Empty(
        val message: String
    ): PlaylistState
}


