package com.practicum.playlistmaker.player.presentation.state


sealed interface TrackState {
    data class TrackInfo(
        val trackName: String,
        val isAlreadyInPlaylist: Boolean,
        val playListName: String?,
        val transactionId: Long?
    ) : TrackState
}
