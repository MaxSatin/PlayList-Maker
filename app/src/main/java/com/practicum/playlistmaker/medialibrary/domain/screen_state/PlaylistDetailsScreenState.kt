package com.practicum.playlistmaker.medialibrary.domain.screen_state

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track

sealed interface PlaylistDetailsScreenState {
    object Loading: PlaylistDetailsScreenState

    data class DetailsState(
        val overallDuration: Long,
        val playlist: Playlist?,
        val contents: List<Track>,
    ): PlaylistDetailsScreenState

    data class Empty(
        val message: String
    ): PlaylistDetailsScreenState
}


