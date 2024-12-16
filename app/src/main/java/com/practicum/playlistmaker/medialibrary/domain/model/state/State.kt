package com.practicum.playlistmaker.medialibrary.domain.model.state

import com.practicum.playlistmaker.medialibrary.domain.screen_state.FavoriteListScreenState
import com.practicum.playlistmaker.medialibrary.domain.screen_state.PlayListsScreenState

data class State(
    val favoriteListScreenState: FavoriteListScreenState,
    val playList: PlayListsScreenState
) {
}