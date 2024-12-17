package com.practicum.playlistmaker.medialibrary.domain.screen_state

sealed interface CreatePlaylistState {
    object CopyExists: CreatePlaylistState
    object NoCopyExists: CreatePlaylistState
}

