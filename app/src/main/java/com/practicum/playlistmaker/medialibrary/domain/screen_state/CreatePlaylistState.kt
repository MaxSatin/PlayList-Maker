package com.practicum.playlistmaker.medialibrary.domain.screen_state

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist

sealed interface CreatePlaylistState {

    data class Content(
        val playLists: List<Playlist>
    ): CreatePlaylistState
}

