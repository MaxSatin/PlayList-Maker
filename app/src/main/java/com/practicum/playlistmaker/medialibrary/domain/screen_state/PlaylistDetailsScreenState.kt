package com.practicum.playlistmaker.medialibrary.domain.screen_state

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track

data class PlaylistDetailsScreenState(
    val isLoading: Boolean,
    val overallDuration: Long,
    val playlist: Playlist?,
    val contents: List<Track>,
    val emptyMessage: String
)

