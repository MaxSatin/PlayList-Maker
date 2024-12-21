package com.practicum.playlistmaker.player.presentation.state

import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist


sealed interface PlayListsScreenState {

    object Loading: PlayListsScreenState

    data class Content(
        val playlists: List<Playlist>
    ): PlayListsScreenState

    data class Empty (
        val message: String
    ): PlayListsScreenState
}