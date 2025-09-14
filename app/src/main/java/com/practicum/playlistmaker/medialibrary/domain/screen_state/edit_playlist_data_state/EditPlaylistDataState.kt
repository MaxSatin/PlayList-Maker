package com.practicum.playlistmaker.medialibrary.domain.screen_state.edit_playlist_data_state

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist

sealed interface EditPlaylistDataState {

    data class Content(
        val playlist: Playlist,
        val fragmentTitle: String,
        val buttonTitle: String
    ): EditPlaylistDataState

    data class Empty(
        val message: String
    ): EditPlaylistDataState
}