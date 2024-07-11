package com.practicum.playlistmaker

data class CurrentTrack(
    val id: String,
    val artistName: String,
    val trackName: String,
    val trackTimeMillis: Long,
    val artworkUrl60: String,
)
