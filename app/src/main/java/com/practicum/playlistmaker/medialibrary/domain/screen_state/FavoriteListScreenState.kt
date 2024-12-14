package com.practicum.playlistmaker.medialibrary.domain.screen_state

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track

sealed interface FavoriteListScreenState {

    object Loading: FavoriteListScreenState

    data class Content(
        val favoriteTrackList: List<Track>
    ): FavoriteListScreenState

    data class Empty (
        val message: String
    ): FavoriteListScreenState
}