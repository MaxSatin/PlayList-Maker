package com.practicum.playlistmaker.medialibrary.domain.model.state

import com.practicum.playlistmaker.medialibrary.domain.screen_state.media_library.FavoriteListScreenState
import com.practicum.playlistmaker.medialibrary.domain.screen_state.media_library.PlayListsScreenState

data class State(
    val favoriteListScreenState: FavoriteListScreenState,
    val playList: PlayListsScreenState
) {
}