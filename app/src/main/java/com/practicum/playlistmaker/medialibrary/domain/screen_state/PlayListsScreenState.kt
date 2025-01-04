package com.practicum.playlistmaker.medialibrary.domain.screen_state

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist

sealed interface PlayListsScreenState {

    object Loading: PlayListsScreenState

    data class Content(
        val playlists: List<Playlist>
    ): PlayListsScreenState

    data class Empty (
        val message: String
    ): PlayListsScreenState
}