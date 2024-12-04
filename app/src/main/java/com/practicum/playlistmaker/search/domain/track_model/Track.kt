package com.practicum.playlistmaker.search.domain.track_model

data class Track(
    val trackId: String,
    val artistName: String,
    val trackName: String,
    val trackTimeMillis: Long,
    val previewUrl: String,
    val artworkUrl60: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    var isFavorite: Boolean
) {
    fun getCoverArtWork(): String{
        return artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    }
}