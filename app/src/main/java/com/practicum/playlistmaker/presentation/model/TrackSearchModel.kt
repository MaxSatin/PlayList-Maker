package com.practicum.playlistmaker.presentation.model

data class TrackSearchModel(
    val trackId: String,
    val artistName: String,
    val trackName: String,
    val trackTimeMillis: Long,
    val artworkUrl60: String,
) {
}