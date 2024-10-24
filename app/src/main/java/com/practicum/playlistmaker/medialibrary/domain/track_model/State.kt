package com.practicum.playlistmaker.medialibrary.domain.track_model

data class State(
    val favoriteTracks: List<Track>?,
    val playList: List<List<Track>>?
) {
}