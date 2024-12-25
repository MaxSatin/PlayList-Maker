package com.practicum.playlistmaker.player.presentation.state


sealed interface TrackState {
    data class CurrentPlaylistStatus(
        private val isAlreadyInPlaylist: Boolean
    ): TrackState
}