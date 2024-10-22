package com.practicum.playlistmaker.medialibrary.domain.track_model

import java.io.Serializable

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
): Serializable {
    fun getCoverArtWork(): String{
        return artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    }
}