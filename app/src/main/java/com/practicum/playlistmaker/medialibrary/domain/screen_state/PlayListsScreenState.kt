package com.practicum.playlistmaker.medialibrary.domain.screen_state

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track

sealed interface PlayListsScreenState {

    object Loading: PlayListsScreenState

    data class Content(
        val favoriteTrackList: List<Playlist>
    ): PlayListsScreenState

    data class Empty (
        val message: String
    ): PlayListsScreenState
}