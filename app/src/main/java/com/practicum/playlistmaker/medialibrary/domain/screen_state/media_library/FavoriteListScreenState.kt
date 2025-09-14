package com.practicum.playlistmaker.medialibrary.domain.screen_state.media_library

import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track

sealed interface FavoriteListScreenState {

    object Loading: FavoriteListScreenState

    data class Content(
        val favoriteTrackList: List<Track>
    ): FavoriteListScreenState

    data class Empty (
        val message: String
    ): FavoriteListScreenState
}