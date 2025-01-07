package com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details

import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track

sealed interface TrackListState {

    data class Contents(
        val trackList: List<Track>
    ): TrackListState

    data class Empty(
        val message: String
    ): TrackListState
}