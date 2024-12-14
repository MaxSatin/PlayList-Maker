package com.practicum.playlistmaker.medialibrary.domain.track_model

import com.practicum.playlistmaker.medialibrary.domain.screen_state.FavoriteListScreenState

data class State(
    val favoriteListScreenState: FavoriteListScreenState,
    val playList: List<List<Track>>?
) {
}