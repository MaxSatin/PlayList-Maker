package com.practicum.playlistmaker

data class CurrentTrack(
    val trackId: String,
    val artistName: String,
    val trackName: String,
    val trackTimeMillis: Long,
    val artworkUrl60: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) {
    fun getCoverArtWork(): String{
        return artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    }
}
