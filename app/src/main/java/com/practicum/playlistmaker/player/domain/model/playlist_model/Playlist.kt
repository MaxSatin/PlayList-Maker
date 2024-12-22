package com.practicum.playlistmaker.player.domain.model.playlist_model

import android.net.Uri

data class Playlist(
    val name: String,
    val description: String,
    val coverUri: Uri?,
    var trackCount: Int,
    var containsCurrentTrack: Boolean
)
