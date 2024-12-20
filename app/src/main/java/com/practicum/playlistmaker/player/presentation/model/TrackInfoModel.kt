package com.practicum.playlistmaker.player.presentation.model


data class TrackInfoModel(
    val trackId: String,
    val artistName: String,
    val trackName: String,
    val trackTimeMillis: String,
    val previewUrl: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val isInFavorite: Boolean,
) {
    fun getCoverArtWork(): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }
}


