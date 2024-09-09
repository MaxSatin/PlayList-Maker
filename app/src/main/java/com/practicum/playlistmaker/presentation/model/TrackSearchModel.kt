package com.practicum.playlistmaker.presentation.model


// Этой модели не нашел применение( Буду очень рад любым комментариям!
data class TrackSearchModel(
    val trackId: String,
    val artistName: String,
    val trackName: String,
    val trackTimeMillis: Long,
    val artworkUrl60: String,
) {
}