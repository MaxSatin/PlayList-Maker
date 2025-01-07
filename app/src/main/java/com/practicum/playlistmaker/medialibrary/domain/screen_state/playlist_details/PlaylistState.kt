package com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist

sealed interface PlaylistState {
//    object Loading: PlaylistState

    data class DetailsState(
//        val overallDuration: Long,
        val playlist: Playlist?,
//        val contents: List<Track>,
    ): PlaylistState

    data class Empty(
        val message: String
    ): PlaylistState
}


