package com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details

sealed interface NavigateFragment {

    data class PlayerFragment(
        val trackGson: String,
    ) : NavigateFragment

    data class EditPlayListFragment(
        val playListGson: String,
    ) : NavigateFragment
}