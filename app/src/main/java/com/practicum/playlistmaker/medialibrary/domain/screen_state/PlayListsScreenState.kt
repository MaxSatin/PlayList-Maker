package com.practicum.playlistmaker.medialibrary.domain.screen_state

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track

sealed interface PlayListsScreenState {

    object Loading: PlayListsScreenState

    data class Content(
        val favoriteTrackList: List<Track>
    ): PlayListsScreenState

    data class Empty (
        val message: String
    ): PlayListsScreenState
}