package com.practicum.playlistmaker.medialibrary.domain.screen_state

sealed interface CreatePlaylistPermissionState {
    object NeededPermission: CreatePlaylistPermissionState
}

